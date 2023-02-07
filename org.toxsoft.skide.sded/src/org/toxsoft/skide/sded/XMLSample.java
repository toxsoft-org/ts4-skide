package org.toxsoft.skide.sded;

import java.io.*;
import java.text.*;

import javax.xml.parsers.*;

import org.jopendocument.dom.spreadsheet.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.w3c.dom.*;

public class XMLSample {

  private final String FILE_post = "Compr2301.PLC_1.OPCUA_20230128-0123.xml";

  /**
   * базовый класс всех узлов сервера OPC UA
   *
   * @author dima
   */
  static class BaseNodeOPC_UA {

    String nodeId;
    String displayName;
    String description;
  }

  /**
   * класс узлов функциональных блоков сервера OPC UA
   *
   * @author dima
   */
  static class FuncBlockOPC_UA
      extends BaseNodeOPC_UA {

    IList<GroupOPC_UA> goups = new ElemArrayList<>();
  }

  /**
   * класс узлов тегов сервера OPC UA
   *
   * @author dima
   */
  static class GroupOPC_UA
      extends BaseNodeOPC_UA {

    IList<TagOPC_UA> goups = new ElemArrayList<>();
  }

  /**
   * класс узлов тегов сервера OPC UA
   *
   * @author dima
   */
  static class TagOPC_UA
      extends BaseNodeOPC_UA {

    String dataType;
  }

  private boolean fileExists( final String path ) {
    File file = new File( FILE_post );
    boolean exists = true;
    if( !file.exists() ) {
      exists = false;
      System.out.println( "FILE NOT EXISTS (" + file.getAbsolutePath() + ")" );
    }
    return exists;
  }

  private String getValue( NodeList fields, int index ) {
    NodeList list = fields.item( index ).getChildNodes();
    if( list.getLength() > 0 ) {
      return list.item( 0 ).getNodeValue();
    }
    else {
      return "";
    }
  }

  @SuppressWarnings( { "null", "nls" } )
  private void readDataXML() {

    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = null;

      FileInputStream fis = null;
      if( fileExists( FILE_post ) ) {
        try {
          fis = new FileInputStream( FILE_post );
          doc = db.parse( fis );
        }
        catch( FileNotFoundException e ) {
          e.printStackTrace();
        }
      }
      // doc.getDocumentElement().normalize();
      Element docEle = doc.getDocumentElement();
      // вот здесь мы уже имеем распарсенный документ, теперь выбираем из него то что нам нужно
      // читаем список узлов верхнего уровня и ищем описания классов
      readClassNodes( docEle );
      // NodeList nl = docEle.getChildNodes();
      //
      // int length = nl.getLength();
      // for( int i = 0; i < length; i++ ) {
      // if( nl.item( i ).getNodeType() == Node.ELEMENT_NODE ) {
      // Element el = (Element)nl.item( i );
      // System.out.println( el.getNodeName() );
      // if( el.getNodeName().contains( "UAObject" ) ) {
      // String name = el.getAttribute( "NodeId" );
      // System.out.println( name );
      // NodeList childs = el.getChildNodes();
      // for( int j = 0; j < childs.getLength(); j++ ) {
      // Node child = childs.item( j );
      // System.out.printf( "node: %s text: %s \n", child.getNodeName(), child.getTextContent() );
      // NamedNodeMap nm = child.getAttributes();
      // if( nm != null ) {
      // for( int k = 0; k < nm.getLength(); k++ ) {
      // Node node = nm.item( k );
      // System.out.printf( "attrName: %s attrVal: %s \n", node.getNodeName(), node.getTextContent() );
      // }
      // }
      // }
      // }
      // }
      // }
      // NodeList nodeList = doc.getElementsByTagName( "row" );
      //
      // for( int s = 0; s < nodeList.getLength(); s++ ) {
      // Node node = nodeList.item( s );
      // if( node.getNodeType() == Node.ELEMENT_NODE ) {
      // Element fstElmnt = (Element)node;
      // NodeList fields = fstElmnt.getElementsByTagName( "field" );
      // Post p = new Post();
      // p.setForum( getValue( fields, 0 ) );
      // p.setDate( sdf.parse( getValue( fields, 1 ) ) );
      // p.setSubject( getValue( fields, 2 ) );
      // p.setUsername( getValue( fields, 4 ) );
      // posts.add( p );
      // }
      // }
    }
    catch( Exception e ) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings( "nls" )
  private static void readClassNodes( Element aDocElement ) {
    NodeList docNodes = aDocElement.getChildNodes();
    int docNodeslength = docNodes.getLength();
    for( int i = 0; i < docNodeslength; i++ ) {
      Node docNode = docNodes.item( i );
      String docNodeName = docNode.getNodeName();
      if( docNodeName.compareTo( "UAObjectType" ) == 0 ) {
        // System.out.printf( "NodeId: %s \n", docNode.getAttributes().getNamedItem( "NodeId" ).getTextContent() );
        // читаем его ребенка DisplayName
        NodeList uaObjectTypeChildNodes = docNode.getChildNodes();
        int uaObjectTypeChildNodesLength = uaObjectTypeChildNodes.getLength();
        for( int j = 0; j < uaObjectTypeChildNodesLength; j++ ) {
          Node uaObjectTypeChildNode = uaObjectTypeChildNodes.item( j );
          if( uaObjectTypeChildNode != null ) {
            String childNodeName = uaObjectTypeChildNode.getNodeName();
            if( childNodeName.compareTo( "DisplayName" ) == 0 ) {
              String fbName = uaObjectTypeChildNode.getTextContent();
              System.out.printf( "function block name: %s\n", fbName );
              if( fbName.compareTo( "AnalogInput" ) == 0 ) {
                // читаем его группы
                String nodeId = docNode.getAttributes().getNamedItem( "NodeId" ).getTextContent();
                readGroupNodes( aDocElement, nodeId );
              }
            }
            if( childNodeName.compareTo( "Description" ) == 0 ) {
              // for example
              // System.out.printf( "attrName: %s attrVal: %s \n", uaObjectTypeChildNode.getNodeName(),
              // uaObjectTypeChildNode.getTextContent() );
              System.out.printf( "function block description: %s\n", uaObjectTypeChildNode.getTextContent() );
            }
          }
        }
      }
    }
  }

  @SuppressWarnings( "nls" )
  private static void readGroupNodes( Element aDocElement, String aParentNodeId ) {
    NodeList docNodes = aDocElement.getChildNodes();
    int docNodeslength = docNodes.getLength();
    for( int i = 0; i < docNodeslength; i++ ) {
      Node uaObjectNode = docNodes.item( i );
      String uaObjectNodeName = uaObjectNode.getNodeName();
      if( uaObjectNodeName.compareTo( "UAObject" ) == 0 ) {
        String parentNodeId = uaObjectNode.getAttributes().getNamedItem( "ParentNodeId" ).getTextContent();
        if( parentNodeId.compareTo( aParentNodeId ) == 0 ) {
          // нод одной из его групп, читаем название
          NodeList uaObjectNodeChildNodes = uaObjectNode.getChildNodes();
          int uaObjectChildNodesLength = uaObjectNodeChildNodes.getLength();
          for( int j = 0; j < uaObjectChildNodesLength; j++ ) {
            Node uaObjectChildNode = uaObjectNodeChildNodes.item( j );
            if( uaObjectChildNode != null ) {
              String childNodeName = uaObjectChildNode.getNodeName();
              if( childNodeName.compareTo( "DisplayName" ) == 0 ) {
                System.out.printf( "group name: %s\n", uaObjectChildNode.getTextContent() );
              }
            }
          }
          // читаем теги этой группы
          String groupNodeId = uaObjectNode.getAttributes().getNamedItem( "NodeId" ).getTextContent();
          readTagNodes( aDocElement, groupNodeId );
        }
      }
    }
  }

  @SuppressWarnings( "nls" )
  private static void readTagNodes( Element aDocElement, String aParentNodeId ) {
    NodeList docNodes = aDocElement.getChildNodes();
    int docNodeslength = docNodes.getLength();
    for( int i = 0; i < docNodeslength; i++ ) {
      Node uaVariableNode = docNodes.item( i );
      String uaVariableNodeName = uaVariableNode.getNodeName();
      if( uaVariableNodeName.compareTo( "UAVariable" ) == 0 ) {
        String parentNodeId = uaVariableNode.getAttributes().getNamedItem( "ParentNodeId" ).getTextContent();
        if( parentNodeId.compareTo( aParentNodeId ) == 0 ) {
          // нод одного из тегов группы, читаем все что нужно
          Node nodeId = uaVariableNode.getAttributes().getNamedItem( "NodeId" );
          System.out.printf( "tag NodeId : %s\n", nodeId.getNodeValue() );
          Node type = uaVariableNode.getAttributes().getNamedItem( "DataType" );
          System.out.printf( "tag DataType : %s\n", type.getTextContent() );
          NodeList uaVariableNodeChildNodes = uaVariableNode.getChildNodes();
          int uaVariableChildNodesLength = uaVariableNodeChildNodes.getLength();
          for( int j = 0; j < uaVariableChildNodesLength; j++ ) {
            Node uaVariableChildNode = uaVariableNodeChildNodes.item( j );
            if( uaVariableChildNode != null ) {
              String childNodeName = uaVariableChildNode.getNodeName();
              if( childNodeName.compareTo( "DisplayName" ) == 0 ) {
                System.out.printf( "tag DisplayName: %s\n", uaVariableChildNode.getTextContent() );
              }
              if( childNodeName.compareTo( "Description" ) == 0 ) {
                Node locale = uaVariableChildNode.getAttributes().getNamedItem( "Locale" );
                if( locale.getTextContent().compareTo( "ru-RU" ) == 0 ) {
                  System.out.printf( "tag Description:  %s\n", uaVariableChildNode.getTextContent() );
                }
              }
            }
          }
        }
      }
    }
  }

  public XMLSample() {
    // Чтение XML-файла
    readDataXML();
  }

  private static final int SHEET_COUNT = 2;

  private static final int COLUMN_COUNT = 10;

  private static final int ROW_COUNT       = 10000;
  /**
   * Номер закладки классов
   */
  private static final int CLASSES_TAB_NUM = 0;

  /**
   * Номер закладки объектов
   */
  private static final int OBJECTS_TAB_NUM = 1;

  public static void main( String[] args ) {
    new XMLSample();
    // сохраняем в файл
    // Создаем собственно таблицу куда будем писать
    SpreadSheet spreadSheet = SpreadSheet.create( SHEET_COUNT, COLUMN_COUNT, ROW_COUNT );
    // Две закладки: классы и объекты
    Sheet classeSheet = spreadSheet.getSheet( CLASSES_TAB_NUM );
    Sheet objectsSheet = spreadSheet.getSheet( OBJECTS_TAB_NUM );
    classeSheet.setName( "Классы" );
    objectsSheet.setName( "Объекты" );
    // Записываем id таблицы и ее название
    // classeSheet.setValueAt( aTedTimeTable.id(), STAGE_ID_COLUMN, TABLE_ID_ROW );

    System.exit( 0 );
  }
}
