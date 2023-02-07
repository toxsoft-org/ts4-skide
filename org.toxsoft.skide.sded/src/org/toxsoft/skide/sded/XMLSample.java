package org.toxsoft.skide.sded;

import java.io.*;
import java.text.*;

import javax.xml.parsers.*;

import org.jopendocument.dom.spreadsheet.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.w3c.dom.*;

public class XMLSample {

  // список функциональных блоков
  private static IListEdit<FuncBlockOPC_UA> funcBlocks = new ElemArrayList<>();

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

    IListEdit<GroupOPC_UA> groups = new ElemArrayList<>();
  }

  /**
   * класс узлов тегов сервера OPC UA
   *
   * @author dima
   */
  static class GroupOPC_UA
      extends BaseNodeOPC_UA {

    IListEdit<TagOPC_UA> tags = new ElemArrayList<>();
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
            FuncBlockOPC_UA funcBlock = new FuncBlockOPC_UA();
            funcBlocks.add( funcBlock );
            String childNodeName = uaObjectTypeChildNode.getNodeName();
            if( childNodeName.compareTo( "DisplayName" ) == 0 ) {
              String fbName = uaObjectTypeChildNode.getTextContent();
              System.out.printf( "function block name: %s\n", fbName );
              funcBlock.displayName = fbName;
            }
            if( childNodeName.compareTo( "Description" ) == 0 ) {
              String fbDescription = uaObjectTypeChildNode.getTextContent();
              System.out.printf( "function block description: %s\n", fbDescription );
              funcBlock.description = fbDescription;
            }
            // читаем его группы
            String nodeId = docNode.getAttributes().getNamedItem( "NodeId" ).getTextContent();
            readGroupNodes( aDocElement, nodeId, funcBlock );
          }
        }
      }
    }
  }

  @SuppressWarnings( "nls" )
  private static void readGroupNodes( Element aDocElement, String aParentNodeId, FuncBlockOPC_UA aFuncBlock ) {
    NodeList docNodes = aDocElement.getChildNodes();
    int docNodeslength = docNodes.getLength();
    for( int i = 0; i < docNodeslength; i++ ) {
      Node uaObjectNode = docNodes.item( i );
      String uaObjectNodeName = uaObjectNode.getNodeName();
      if( uaObjectNodeName.compareTo( "UAObject" ) == 0 ) {
        GroupOPC_UA group = new GroupOPC_UA();
        aFuncBlock.groups.add( group );
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
                String groupName = uaObjectChildNode.getTextContent();
                System.out.printf( "group name: %s\n", groupName );
                group.displayName = groupName;
                // TODO нет такого нода в файле
                group.description = groupName;
              }
            }
          }
          // читаем теги этой группы
          String groupNodeId = uaObjectNode.getAttributes().getNamedItem( "NodeId" ).getTextContent();
          readTagNodes( aDocElement, groupNodeId, group );
        }
      }
    }
  }

  @SuppressWarnings( "nls" )
  private static void readTagNodes( Element aDocElement, String aParentNodeId, GroupOPC_UA aGroup ) {
    NodeList docNodes = aDocElement.getChildNodes();
    int docNodeslength = docNodes.getLength();
    for( int i = 0; i < docNodeslength; i++ ) {
      Node uaVariableNode = docNodes.item( i );
      String uaVariableNodeName = uaVariableNode.getNodeName();
      if( uaVariableNodeName.compareTo( "UAVariable" ) == 0 ) {
        String parentNodeId = uaVariableNode.getAttributes().getNamedItem( "ParentNodeId" ).getTextContent();
        if( parentNodeId.compareTo( aParentNodeId ) == 0 ) {
          TagOPC_UA tag = new TagOPC_UA();
          aGroup.tags.add( tag );
          // нод одного из тегов группы, читаем все что нужно
          Node nodeId = uaVariableNode.getAttributes().getNamedItem( "NodeId" );
          tag.nodeId = nodeId.getTextContent();
          System.out.printf( "tag NodeId : %s\n", nodeId.getNodeValue() );
          Node type = uaVariableNode.getAttributes().getNamedItem( "DataType" );
          System.out.printf( "tag DataType : %s\n", type.getTextContent() );
          tag.dataType = type.getTextContent();
          NodeList uaVariableNodeChildNodes = uaVariableNode.getChildNodes();
          int uaVariableChildNodesLength = uaVariableNodeChildNodes.getLength();
          for( int j = 0; j < uaVariableChildNodesLength; j++ ) {
            Node uaVariableChildNode = uaVariableNodeChildNodes.item( j );
            if( uaVariableChildNode != null ) {
              String childNodeName = uaVariableChildNode.getNodeName();
              if( childNodeName.compareTo( "DisplayName" ) == 0 ) {
                String displayName = uaVariableChildNode.getTextContent();
                System.out.printf( "tag DisplayName: %s\n", displayName );
                tag.displayName = displayName;
              }
              if( childNodeName.compareTo( "Description" ) == 0 ) {
                Node locale = uaVariableChildNode.getAttributes().getNamedItem( "Locale" );
                if( locale.getTextContent().compareTo( "ru-RU" ) == 0 ) {
                  String description = uaVariableChildNode.getTextContent();
                  System.out.printf( "tag Description:  %s\n", uaVariableChildNode.getTextContent() );
                  tag.description = description;
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

  private static final int FUNC_BLOCK_COLUMN = 1;

  private static final int GROUP_COLUMN = 2;

  private static final int TAG_NAME_COLUMN = 3;

  private static final int TAG_DESCR_COLUMN = 4;

  private static final int TAG_NODE_ID_COLUMN = 5;

  private static final int TAG_TYPE_COLUMN = 6;

  public static void main( String[] args ) {
    new XMLSample();
    // сохраняем в файл
    File targetFile = new File( "tagsOPC_UA.ods" );

    // Создаем собственно таблицу куда будем писать
    SpreadSheet spreadSheet = SpreadSheet.create( SHEET_COUNT, COLUMN_COUNT, ROW_COUNT );
    // Две закладки: классы и объекты
    Sheet classeSheet = spreadSheet.getSheet( CLASSES_TAB_NUM );
    Sheet objectsSheet = spreadSheet.getSheet( OBJECTS_TAB_NUM );
    classeSheet.setName( "Классы" );
    objectsSheet.setName( "Объекты" );
    // Записываем в файл то что считали
    int currRow = 1;
    for( FuncBlockOPC_UA funcBlock : funcBlocks ) {
      if( funcBlock.displayName != null ) {
        classeSheet.setValueAt( funcBlock.displayName, FUNC_BLOCK_COLUMN, currRow );
        currRow += writeGroups( classeSheet, funcBlock, currRow );
      }
    }
    // Сохраняем в файл
    try {
      spreadSheet.saveAs( targetFile );
    }
    catch( IOException ex ) {
      LoggerUtils.errorLogger().error( ex );
    }

    System.exit( 0 );
  }

  private static int writeGroups( Sheet aClasseSheet, FuncBlockOPC_UA aFuncBlock, int aCurrRow ) {
    int retVal = 0;
    int currRow = aCurrRow;
    for( GroupOPC_UA group : aFuncBlock.groups ) {
      if( !group.tags.isEmpty() ) {
        aClasseSheet.setValueAt( group.displayName, GROUP_COLUMN, currRow );
        int rowQtty = writeTags( aClasseSheet, group, currRow, aFuncBlock.displayName );
        currRow += rowQtty;
        retVal += rowQtty;
        aClasseSheet.setValueAt( aFuncBlock.displayName, FUNC_BLOCK_COLUMN, currRow );
      }
    }
    return retVal;
  }

  private static int writeTags( Sheet aClasseSheet, GroupOPC_UA aGroup, int aCurrRow, String aFbDisplayName ) {
    int retVal = 0;
    int currRow = aCurrRow;
    for( TagOPC_UA tag : aGroup.tags ) {
      aClasseSheet.setValueAt( tag.displayName, TAG_NAME_COLUMN, currRow );
      aClasseSheet.setValueAt( tag.description, TAG_DESCR_COLUMN, currRow );
      aClasseSheet.setValueAt( tag.nodeId, TAG_NODE_ID_COLUMN, currRow );
      aClasseSheet.setValueAt( tag.dataType, TAG_TYPE_COLUMN, currRow );
      retVal++;
      currRow++;
      aClasseSheet.setValueAt( aGroup.displayName, GROUP_COLUMN, currRow );
      aClasseSheet.setValueAt( aFbDisplayName, FUNC_BLOCK_COLUMN, currRow );
    }
    return retVal;
  }
}
