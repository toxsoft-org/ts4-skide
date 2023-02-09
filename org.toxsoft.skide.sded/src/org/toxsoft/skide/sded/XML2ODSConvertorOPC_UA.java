package org.toxsoft.skide.sded;

import java.io.*;

import javax.xml.parsers.*;

import org.jopendocument.dom.spreadsheet.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.w3c.dom.*;

/**
 * Утилита для конвертации описания тегов OPC UA из XML в файл формата ODS
 *
 * @author dima
 */
public class XML2ODSConvertorOPC_UA {

  // список глобальных блоков
  private static IListEdit<DataBlocksGlobalOPC_UA> globalDataBlocks = new ElemArrayList<>();
  // список функциональных блоков
  private static IListEdit<FuncBlockOPC_UA> funcBlocks = new ElemArrayList<>();
  // список объектов
  private static IListEdit<ObjectOPC_UA> uaObjects = new ElemArrayList<>();

  private final String sourceXML = "Compr2301.PLC_1.OPCUA_20230209-1058.xml";

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
   * класс узлов блоков глобальных данных сервера OPC UA
   *
   * @author dima
   */
  static class DataBlocksGlobalOPC_UA
      extends BaseNodeOPC_UA {

    IListEdit<TagOPC_UA> tags = new ElemArrayList<>();

    DataBlocksGlobalOPC_UA( String aDisplayName ) {
      displayName = aDisplayName;
    }
  }

  /**
   * класс узлов функциональных блоков сервера OPC UA
   *
   * @author dima
   */
  static class FuncBlockOPC_UA
      extends BaseNodeOPC_UA {

    IListEdit<GroupOPC_UA> groups = new ElemArrayList<>();

    FuncBlockOPC_UA( String aDisplayName ) {
      displayName = aDisplayName;
    }
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

  /**
   * класс объектов сервера OPC UA
   *
   * @author dima
   */
  static class ObjectOPC_UA
      extends BaseNodeOPC_UA {

    IListEdit<GroupOPC_UA> groups = new ElemArrayList<>();
    String                 className;

    ObjectOPC_UA( String aClassName, String aObjName ) {
      className = aClassName;
      displayName = aObjName;
    }
  }

  private boolean fileExists( final String path ) {
    File file = new File( sourceXML );
    boolean exists = true;
    if( !file.exists() ) {
      exists = false;
      System.out.println( "FILE NOT EXISTS (" + file.getAbsolutePath() + ")" );
    }
    return exists;
  }

  @SuppressWarnings( { "null" } )
  private void readDataXML() {

    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = null;

      FileInputStream fis = null;
      if( fileExists( sourceXML ) ) {
        try {
          fis = new FileInputStream( sourceXML );
          doc = db.parse( fis );
        }
        catch( FileNotFoundException e ) {
          e.printStackTrace();
        }
        finally {
          fis.close();
        }
      }
      // doc.getDocumentElement().normalize();
      Element docEle = doc.getDocumentElement();
      // вот здесь мы уже имеем распарсенный документ, теперь выбираем из него то что нам нужно
      // читаем список узлов верхнего уровня и ищем описания классов
      readClassNodes( docEle );
      readObjectNodes( docEle );
    }
    catch( Exception e ) {
      e.printStackTrace();
    }
  }

  private static void readClassNodes( Element aDocElement ) {
    NodeList docNodes = aDocElement.getChildNodes();
    int docNodeslength = docNodes.getLength();
    for( int i = 0; i < docNodeslength; i++ ) {
      Node docNode = docNodes.item( i );
      if( isFuncBlockDefinition( docNode ) ) {
        readFuncBlock( aDocElement, docNode );
        continue;
      }
      if( isDataBlockGlobal( docNode ) ) {
        readGlobalBlock( aDocElement, docNode );
        continue;
      }
    }
  }

  @SuppressWarnings( "nls" )
  private static void readGlobalBlock( Element aDocElement, Node docNode ) {
    // читаем его ребенка DisplayName
    NodeList uaObjectTypeChildNodes = docNode.getChildNodes();
    int uaObjectTypeChildNodesLength = uaObjectTypeChildNodes.getLength();
    for( int j = 0; j < uaObjectTypeChildNodesLength; j++ ) {
      Node uaObjectTypeChildNode = uaObjectTypeChildNodes.item( j );
      DataBlocksGlobalOPC_UA dataBlocksGlobal = null;
      if( uaObjectTypeChildNode != null ) {
        String childNodeName = uaObjectTypeChildNode.getNodeName();
        if( childNodeName.compareTo( "DisplayName" ) == 0 ) {
          String fbName = uaObjectTypeChildNode.getTextContent();
          System.out.printf( "global block name: %s\n", fbName );
          if( fbName.compareTo( "OS" ) == 0 ) {
            System.out.printf( "!!!" );
          }
          dataBlocksGlobal = new DataBlocksGlobalOPC_UA( fbName );
          globalDataBlocks.add( dataBlocksGlobal );
          dataBlocksGlobal.nodeId = docNode.getAttributes().getNamedItem( "NodeId" ).getTextContent();
        }
        // читаем его теги
        if( dataBlocksGlobal != null ) {
          dataBlocksGlobal.tags.addAll( readTagNodes( aDocElement, dataBlocksGlobal.nodeId ) );
        }
      }
    }
  }

  @SuppressWarnings( "nls" )
  private static void readFuncBlock( Element aDocElement, Node docNode ) {
    // читаем его ребенка DisplayName
    NodeList uaObjectTypeChildNodes = docNode.getChildNodes();
    int uaObjectTypeChildNodesLength = uaObjectTypeChildNodes.getLength();
    for( int j = 0; j < uaObjectTypeChildNodesLength; j++ ) {
      Node uaObjectTypeChildNode = uaObjectTypeChildNodes.item( j );
      FuncBlockOPC_UA funcBlock = null;
      if( uaObjectTypeChildNode != null ) {
        String childNodeName = uaObjectTypeChildNode.getNodeName();
        if( childNodeName.compareTo( "DisplayName" ) == 0 ) {
          String fbName = uaObjectTypeChildNode.getTextContent();
          System.out.printf( "function block name: %s\n", fbName );
          funcBlock = new FuncBlockOPC_UA( fbName );
          funcBlocks.add( funcBlock );
          funcBlock.nodeId = docNode.getAttributes().getNamedItem( "NodeId" ).getTextContent();
        }
        // читаем его группы
        if( funcBlock != null ) {
          String nodeId = docNode.getAttributes().getNamedItem( "NodeId" ).getTextContent();
          readGroupNodes( aDocElement, nodeId, funcBlock );
        }
      }
    }
  }

  /**
   * Опредяет что тут описание функционального блока
   *
   * @param aDocNode узел
   * @return true - описание функц. блока
   */
  @SuppressWarnings( "nls" )
  private static boolean isFuncBlockDefinition( Node aDocNode ) {
    boolean retVal = false;
    String docNodeName = aDocNode.getNodeName();
    // если узел UAObjectType, то это 100% func block
    if( docNodeName.compareTo( "UAObjectType" ) == 0 ) {
      retVal = true;
    }
    return retVal;
  }

  /**
   * Опредяет что тут описание глобального глобального блока
   *
   * @param aDocNode узел
   * @return true - описание функц. блока
   */
  private static boolean isDataBlockGlobal( Node aDocNode ) {
    boolean retVal = false;
    // если узла есть attr ParentNodeId и ParentNodeId="ns=3;s=DataBlocksGlobal"
    NamedNodeMap nodeAttrMap = aDocNode.getAttributes();
    if( nodeAttrMap != null ) {
      Node parentNodeAttr = nodeAttrMap.getNamedItem( "ParentNodeId" );
      if( parentNodeAttr != null ) {
        String parentNodeId = parentNodeAttr.getTextContent();
        if( parentNodeId.compareTo( "ns=3;s=DataBlocksGlobal" ) == 0 ) {
          retVal = true;
        }
      }
    }
    return retVal;
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
          group.tags.addAll( readTagNodes( aDocElement, groupNodeId ) );
        }
      }
    }
  }

  static String readDislayName( Node uaObjectNode ) {
    NodeList uaObjectNodeChildNodes = uaObjectNode.getChildNodes();
    int uaObjectChildNodesLength = uaObjectNodeChildNodes.getLength();
    for( int j = 0; j < uaObjectChildNodesLength; j++ ) {
      Node uaObjectChildNode = uaObjectNodeChildNodes.item( j );
      if( uaObjectChildNode != null ) {
        String childNodeName = uaObjectChildNode.getNodeName();
        if( childNodeName.compareTo( "DisplayName" ) == 0 ) {
          return uaObjectChildNode.getTextContent();
        }
      }
    }
    return null;
  }

  @SuppressWarnings( "nls" )
  private static void readObjectNodes( Element aDocElement ) {
    NodeList docNodes = aDocElement.getChildNodes();
    int docNodeslength = docNodes.getLength();
    for( int i = 0; i < docNodeslength; i++ ) {
      Node uaObjectNode = docNodes.item( i );
      String uaObjectNodeName = uaObjectNode.getNodeName();
      if( uaObjectNodeName.compareTo( "UAObject" ) == 0 ) {
        // ищем у него child
        NodeList uaObjectNodeChildNodes = uaObjectNode.getChildNodes();
        int uaObjectChildNodesLength = uaObjectNodeChildNodes.getLength();
        for( int j = 0; j < uaObjectChildNodesLength; j++ ) {
          Node uaObjectChildNode = uaObjectNodeChildNodes.item( j );
          if( uaObjectChildNode != null ) {
            String childNodeName = uaObjectChildNode.getNodeName();
            if( childNodeName.compareTo( "References" ) == 0 ) {
              // нашли описание связей, ищем в его детях Reference с атрибутом ReferenceType="HasTypeDefinition"
              NodeList referenciesdNodes = uaObjectChildNode.getChildNodes();
              int referenciesLength = referenciesdNodes.getLength();
              for( int k = 0; k < referenciesLength; k++ ) {
                Node referenceNode = referenciesdNodes.item( k );
                if( referenceNode != null ) {
                  String referenceNodeName = referenceNode.getNodeName();
                  if( referenceNodeName.compareTo( "Reference" ) == 0 ) {
                    // читаем его атрибут ReferenceType="HasTypeDefinition"
                    Node referenceTypeNode = referenceNode.getAttributes().getNamedItem( "ReferenceType" );
                    if( referenceTypeNode != null ) {
                      String referenceTypeValue = referenceTypeNode.getTextContent();
                      if( referenceTypeValue.compareTo( "HasTypeDefinition" ) == 0 ) {
                        // запоминаем NodeId функционального блока
                        String fbNodeId = referenceNode.getTextContent();
                        System.out.printf( "ReferenceType: %s \n", fbNodeId );
                        // ищем его класс
                        FuncBlockOPC_UA fb = findFuncBlock( fbNodeId );
                        if( fb != null ) {
                          System.out.printf( "FuncBlockOPC_UA: %s \n", fb.displayName );
                          // создаем новый объект
                          String objName = readDislayName( uaObjectNode );
                          ObjectOPC_UA uaObject = new ObjectOPC_UA( fb.displayName, objName );
                          // запоминаем
                          uaObjects.add( uaObject );
                          // читаем его группы
                          String nodeId = uaObjectNode.getAttributes().getNamedItem( "NodeId" ).getTextContent();
                          readObjectGroupNodes( aDocElement, nodeId, uaObject );
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  @SuppressWarnings( "nls" )
  private static void readObjectGroupNodes( Element aDocElement, String aParentNodeId, ObjectOPC_UA aObjectOPC_UA ) {
    NodeList docNodes = aDocElement.getChildNodes();
    int docNodeslength = docNodes.getLength();
    for( int i = 0; i < docNodeslength; i++ ) {
      Node uaObjectNode = docNodes.item( i );
      String uaObjectNodeName = uaObjectNode.getNodeName();
      if( uaObjectNodeName.compareTo( "UAObject" ) == 0 ) {
        GroupOPC_UA group = new GroupOPC_UA();
        aObjectOPC_UA.groups.add( group );
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
          group.tags.addAll( readTagNodes( aDocElement, groupNodeId ) );
        }
      }
    }
  }

  /**
   * Ищет функциональный блок с заданным NodeId
   *
   * @param aFbNodeId NodeId
   * @return найденый {@link FuncBlockOPC_UA} или null
   */
  private static FuncBlockOPC_UA findFuncBlock( String aFbNodeId ) {
    for( FuncBlockOPC_UA fb : funcBlocks ) {
      if( fb.nodeId.compareTo( aFbNodeId ) == 0 ) {
        return fb;
      }
    }
    return null;
  }

  @SuppressWarnings( "nls" )
  private static IList<TagOPC_UA> readTagNodes( Element aDocElement, String aParentNodeId ) {
    IListEdit<TagOPC_UA> retVal = new ElemArrayList<>();
    NodeList docNodes = aDocElement.getChildNodes();
    int docNodeslength = docNodes.getLength();
    for( int i = 0; i < docNodeslength; i++ ) {
      Node uaVariableNode = docNodes.item( i );
      String uaVariableNodeName = uaVariableNode.getNodeName();
      if( uaVariableNodeName.compareTo( "UAVariable" ) == 0 ) {
        String parentNodeId = uaVariableNode.getAttributes().getNamedItem( "ParentNodeId" ).getTextContent();
        if( parentNodeId.compareTo( aParentNodeId ) == 0 ) {
          TagOPC_UA tag = new TagOPC_UA();
          // aGroup.tags.add( tag );
          retVal.add( tag );
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
    return retVal;
  }

  public XML2ODSConvertorOPC_UA() {
    // Чтение XML-файла
    readDataXML();
  }

  private static final int SHEET_COUNT = 3;

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

  /**
   * Номер закладки для Сереги
   */
  private static final int FOR_SERG_TAB_NUM = 2;

  private static final int FUNC_BLOCK_COLUMN = 2;

  private static final int GROUP_COLUMN = 3;

  private static final int TAG_NAME_COLUMN = 4;

  private static final int TAG_DESCR_COLUMN = 5;

  private static final int TAG_NODE_ID_COLUMN = 6;

  private static final int TAG_TYPE_COLUMN = 7;

  // public static class OPC_UA_FullPath {
  //
  // String namespace;
  // String nodeId;
  // }
  //
  // public static OPC_UA_FullPath parseFullPath( String aString ) {
  // OPC_UA_FullPath retVal = new OPC_UA_FullPath();
  // Pattern p = Pattern.compile( "ns=([\\d]+);s=([a-z.\"_A-Z0-9]+)" );
  // Matcher n = p.matcher( aString );
  // if( n.find() ) {
  // retVal.namespace = n.group( 1 );
  // System.out.printf( "namespace = %s\n", retVal.namespace ); //
  // retVal.nodeId = n.group( 2 );
  // System.out.printf( "nodeId = %s\n", retVal.nodeId ); //
  // }
  // return retVal;
  // }

  public static void main( String[] args ) {
    // parseFullPath( "ns=3;s=\"ST5\".\"PV\"" );
    // parseFullPath( "ns=3;s=\"Clock_0-5Hz\"" );
    // parseFullPath( "ns=3;s=\"P10_InletAirConsumption\"" );
    // parseFullPath( "ns=3;s=\"reserv73\"" );
    // parseFullPath( "ns=3;s=\"PPK_Start2Close\"" );
    // parseFullPath( "ns=3;s=\"AiTemp\".\"_PV\"" );
    // parseFullPath( "ns=3;s=\"DB_PID_DZ\".I_ITL_ON" );

    new XML2ODSConvertorOPC_UA();
    // сохраняем в файл
    File targetFile = new File( "tagsOPC_UA.ods" );

    // Создаем собственно таблицу куда будем писать
    SpreadSheet spreadSheet = SpreadSheet.create( SHEET_COUNT, COLUMN_COUNT, ROW_COUNT );
    // Две закладки: классы и объекты
    Sheet classeSheet = spreadSheet.getSheet( CLASSES_TAB_NUM );
    Sheet objectsSheet = spreadSheet.getSheet( OBJECTS_TAB_NUM );
    Sheet forSerg1Sheet = spreadSheet.getSheet( FOR_SERG_TAB_NUM );
    classeSheet.setName( "Классы" );
    objectsSheet.setName( "Объекты" );
    forSerg1Sheet.setName( "Для Сергея" );
    // Записываем в файл то что считали
    // заголовки столбцов для закладки описания классов
    classeSheet.setValueAt( "FB_id", FUNC_BLOCK_COLUMN, 0 );
    classeSheet.setValueAt( "tag name", TAG_NAME_COLUMN, 0 );
    classeSheet.setValueAt( "tag description", TAG_DESCR_COLUMN, 0 );
    classeSheet.setValueAt( "full node path", TAG_NODE_ID_COLUMN, 0 );
    classeSheet.setValueAt( "tag type", TAG_TYPE_COLUMN, 0 );
    // заголовки столбцов для закладки описания классов
    objectsSheet.setValueAt( "FB_id", FUNC_BLOCK_COLUMN, 0 );
    objectsSheet.setValueAt( "dataBlockId", 3, 0 );
    objectsSheet.setValueAt( "tag name", 4, 0 );
    objectsSheet.setValueAt( "tag description", 5, 0 );
    objectsSheet.setValueAt( "full node path", 6, 0 );
    objectsSheet.setValueAt( "tag type", 7, 0 );

    int classesSheetCurrRow = 1;
    for( DataBlocksGlobalOPC_UA globalDataBlock : globalDataBlocks ) {
      if( globalDataBlock.displayName != null ) {
        classeSheet.setValueAt( globalDataBlock.displayName, FUNC_BLOCK_COLUMN, classesSheetCurrRow );
        classesSheetCurrRow +=
            writeTags( classeSheet, globalDataBlock.tags, classesSheetCurrRow, globalDataBlock.displayName, "" );
      }
    }
    for( FuncBlockOPC_UA funcBlock : funcBlocks ) {
      if( funcBlock.displayName != null ) {
        classeSheet.setValueAt( funcBlock.displayName, FUNC_BLOCK_COLUMN, classesSheetCurrRow );
        classesSheetCurrRow += writeGroups( classeSheet, funcBlock, classesSheetCurrRow );
      }
    }
    int objsSheetCurrRow = 1;
    for( DataBlocksGlobalOPC_UA uaBlocksGlobalOPC_UA : globalDataBlocks ) {
      if( uaBlocksGlobalOPC_UA.displayName != null ) {
        objectsSheet.setValueAt( uaBlocksGlobalOPC_UA.displayName, FUNC_BLOCK_COLUMN, objsSheetCurrRow );
        // у глобальных блоков название блока совпадает с название объекта
        objectsSheet.setValueAt( uaBlocksGlobalOPC_UA.displayName, FUNC_BLOCK_COLUMN + 1, objsSheetCurrRow );
        objsSheetCurrRow += writeGlobalBlock( objectsSheet, uaBlocksGlobalOPC_UA, objsSheetCurrRow );
      }
    }
    for( ObjectOPC_UA uaObj : uaObjects ) {
      if( uaObj.displayName != null ) {
        objectsSheet.setValueAt( uaObj.className, FUNC_BLOCK_COLUMN, objsSheetCurrRow );
        objsSheetCurrRow += writeObjGroups( objectsSheet, uaObj, objsSheetCurrRow );
      }
    }
    int forSergSheetCurrRow = 1;
    for( DataBlocksGlobalOPC_UA uaBlocksGlobalOPC_UA : globalDataBlocks ) {
      if( uaBlocksGlobalOPC_UA.displayName != null ) {
        forSerg1Sheet.setValueAt( uaBlocksGlobalOPC_UA.displayName, FUNC_BLOCK_COLUMN, forSergSheetCurrRow );
        // у глобальных блоков название блока совпадает с название объекта
        forSerg1Sheet.setValueAt( uaBlocksGlobalOPC_UA.displayName, FUNC_BLOCK_COLUMN + 1, forSergSheetCurrRow );
        forSergSheetCurrRow++;
      }
    }
    for( ObjectOPC_UA uaObj : uaObjects ) {
      if( uaObj.displayName != null ) {
        forSerg1Sheet.setValueAt( uaObj.className, FUNC_BLOCK_COLUMN, forSergSheetCurrRow );
        forSerg1Sheet.setValueAt( uaObj.displayName, FUNC_BLOCK_COLUMN + 1, forSergSheetCurrRow );
        forSergSheetCurrRow++;
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

  private static int writeObjGroups( Sheet aObjectsSheet, ObjectOPC_UA aUaObj, int aCurrRow ) {
    int retVal = 0;
    int currRow = aCurrRow;
    for( GroupOPC_UA group : aUaObj.groups ) {
      if( !group.tags.isEmpty() ) {
        aObjectsSheet.setValueAt( aUaObj.displayName, GROUP_COLUMN, currRow );
        int rowQtty = writeObjTags( aObjectsSheet, group, currRow, aUaObj );
        currRow += rowQtty;
        retVal += rowQtty;
        aObjectsSheet.setValueAt( aUaObj.className, FUNC_BLOCK_COLUMN, currRow );
      }
    }
    return retVal;
  }

  private static int writeGlobalBlock( Sheet aClasseSheet, DataBlocksGlobalOPC_UA aBlockGlobal, int aCurrRow ) {
    int retVal = 0;
    int currRow = aCurrRow;
    if( !aBlockGlobal.tags.isEmpty() ) {
      aClasseSheet.setValueAt( aBlockGlobal.displayName, GROUP_COLUMN, currRow );
      int rowQtty =
          writeTags( aClasseSheet, aBlockGlobal.tags, currRow, aBlockGlobal.displayName, aBlockGlobal.displayName );
      currRow += rowQtty;
      retVal += rowQtty;
      aClasseSheet.setValueAt( aBlockGlobal.displayName, FUNC_BLOCK_COLUMN, currRow );
    }
    return retVal;
  }

  private static int writeGroups( Sheet aClasseSheet, FuncBlockOPC_UA aFuncBlock, int aCurrRow ) {
    int retVal = 0;
    int currRow = aCurrRow;
    for( GroupOPC_UA group : aFuncBlock.groups ) {
      if( !group.tags.isEmpty() ) {
        aClasseSheet.setValueAt( group.displayName, GROUP_COLUMN, currRow );
        int rowQtty = writeTags( aClasseSheet, group.tags, currRow, aFuncBlock.displayName, group.displayName );
        currRow += rowQtty;
        retVal += rowQtty;
        aClasseSheet.setValueAt( aFuncBlock.displayName, FUNC_BLOCK_COLUMN, currRow );
      }
    }
    return retVal;
  }

  private static int writeTags( Sheet aSheet, IList<TagOPC_UA> aTags, int aCurrRow, String aFbDisplayName,
      String aGroupDisplayName ) {
    int retVal = 0;
    int currRow = aCurrRow;
    for( TagOPC_UA tag : aTags ) {
      aSheet.setValueAt( tag.displayName, TAG_NAME_COLUMN, currRow );
      aSheet.setValueAt( tag.description, TAG_DESCR_COLUMN, currRow );
      aSheet.setValueAt( tag.nodeId, TAG_NODE_ID_COLUMN, currRow );
      aSheet.setValueAt( tag.dataType, TAG_TYPE_COLUMN, currRow );
      retVal++;
      currRow++;
      aSheet.setValueAt( aFbDisplayName, FUNC_BLOCK_COLUMN, currRow );
      aSheet.setValueAt( aGroupDisplayName, GROUP_COLUMN, currRow );
    }
    return retVal;
  }

  private static int writeObjTags( Sheet aSheet, GroupOPC_UA aGroup, int aCurrRow, ObjectOPC_UA aUaObj ) {
    int retVal = 0;
    int currRow = aCurrRow;
    for( TagOPC_UA tag : aGroup.tags ) {
      aSheet.setValueAt( tag.displayName, TAG_NAME_COLUMN, currRow );
      aSheet.setValueAt( tag.description, TAG_DESCR_COLUMN, currRow );
      aSheet.setValueAt( tag.nodeId, TAG_NODE_ID_COLUMN, currRow );
      aSheet.setValueAt( tag.dataType, TAG_TYPE_COLUMN, currRow );
      retVal++;
      currRow++;
      aSheet.setValueAt( aUaObj.displayName, GROUP_COLUMN, currRow );
      aSheet.setValueAt( aUaObj.className, FUNC_BLOCK_COLUMN, currRow );
    }
    return retVal;
  }

}
