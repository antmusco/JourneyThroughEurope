package Journey.file;

import Journey.game.CityNode;
import Journey.game.Edge;
import Journey.game.Edge.EdgeType;
import Journey.game.GameProperties;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import properties_manager.PropertiesManager;
import xml_utilities.InvalidXMLFileFormatException;

/**
 * Class responsible for loading the game files for the Journey Framework.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Oct-22-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class FileLoader {
    
    static String dataPath;
    static String mapFile;
    static String nodeTag;
    static String name;
    static String quarter;
    static String color;
    static String isFlight;  
    static String xPos;     
    static String yPos;
    static String flightX;
    static String flightY;
    static String cardFrontFile;
    static String cardBackFile;
    static String edgesTag;
    static String instructions;   
    
    /**
     * Static method which constructs the list of CityNodes from the XML file
     * located in the 'data' folder. If any error occurs, null is returned.
     * 
     * @return
     *          An ArrayList of CityNodes containing the list of CityNodes 
     *          within the game. 
     */
    public static HashMap<String, CityNode> loadMap() {
     
        /* Create the list of CityNodes  */
        HashMap<String, CityNode> list = new HashMap<>();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        /* Get path to the file and strings for parsing tags. */
        dataPath = props.getProperty(GameProperties.DATA_PATH);
        mapFile = props.getProperty(GameProperties.MAP_FILE_NAME);
        nodeTag = props.getProperty(GameProperties.CITY_NODE_TAG);
        name = props.getProperty(GameProperties.NAME_TAG);
        quarter = props.getProperty(GameProperties.QUARTER_TAG);
        color = props.getProperty(GameProperties.COLOR_TAG);
        isFlight = props.getProperty(GameProperties.FLIGHT_TAG);
        flightX = props.getProperty(GameProperties.FLIGHTX_TAG);
        flightY = props.getProperty(GameProperties.FLIGHTY_TAG);
        xPos = props.getProperty(GameProperties.XPOS_TAG);
        yPos = props.getProperty(GameProperties.YPOS_TAG);
        cardFrontFile = props.getProperty(GameProperties.CARD_FRONT_TAG);
        cardBackFile = props.getProperty(GameProperties.CARD_BACK_TAG);
        edgesTag = props.getProperty(GameProperties.EDGES_TAG);
        instructions = props.getProperty(GameProperties.INSTRUCTIONS_TAG);
        
        try {
            
            /* Build the document to parse the City Nodes. */
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("./data/map.xml");
            
            /* Get the list of CityNodes. */
            NodeList cities = doc.getElementsByTagName("CityNode");
            System.out.println(cities.getLength());

            /* Loop through each CityNode in the list. */
            for(int i = 0; i < cities.getLength(); i++) {
                
                /* Add the new CityNode to the list. */
                Node cityNode  = cities.item(i);
                CityNode cn = buildCityNode(cityNode);
                list.put(cn.getName(), cn);
                
            }     
            
            for (Map.Entry<String, CityNode> entry : list.entrySet()) {
               
                CityNode node = entry.getValue();
                for(Edge e : node.getEdges())
                    e.construct(node, list);
                
            }
            
            /* Return the list of CityNodes */
            return list;            
            
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (InvalidXMLFileFormatException ex) {
            ex.printStackTrace();
        }
    
        /* If an error is encountered, return null. */
        return null;
        
    }

    /**
     * Builds and returns a CityNode object based on an XML Node. If an error
     * is encountered, an Exception is thrown.cityNode@param c
     *          XML Node describing the attributes of the CityNode.
     * @return 
     *          A constructed CityNode object based on the attributes in the
     *          XML Node.
     */
    private static CityNode buildCityNode(Node cityNode) throws InvalidXMLFileFormatException {
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(GameProperties.IMG_PATH);
        
        Color cityColor = null;
        String cityName = null;
        Image cardFront = null;
        Image cardBack = null;
        double cityXPos = 0;
        double cityYPos = 0;
        int quarterVal = 0;
        double flightXPos = 0;
        double flightYPos = 0;
        boolean cityIsFlight = false;
        ArrayList<Edge> edges = new ArrayList<>();
        
        /* Ensure the element is an element node, cast to Element. */
        if(cityNode.getNodeType() != Node.ELEMENT_NODE) {
            throw new InvalidXMLFileFormatException("Shit is whack.");
        }   

        Element cityElement = (Element) cityNode;

        /* Loop through all of the attributes of the CityNode. */
        NodeList cityAttributes = cityElement.getChildNodes();
        for(int j = 0; j < cityAttributes.getLength(); j++) {

            Node a = cityAttributes.item(j);

            /* Ensure the element is an element node, cast to Element. */
            if(a.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            Element m = (Element) a;
            String tagName = m.getTagName();

            /* Switch on the tagName. */
            if(tagName.equals(name)){

                cityName = m.getTextContent();
                
            } 
            else if(tagName.equals(quarter)) {

                quarterVal = Integer.parseInt(m.getTextContent());
                
            } 
            else if(tagName.equals(color)) {

                cityColor = Color.valueOf(m.getTextContent());
                
            } 
            else if(tagName.equals(isFlight)) {

                String bool = m.getTextContent();
                cityIsFlight = Boolean.valueOf(bool);

            } 
            else if(tagName.equals(xPos)) {

                cityXPos = Double.parseDouble(m.getTextContent());

            } 
            else if(tagName.equals(yPos)) {

                cityYPos = Double.parseDouble(m.getTextContent());

            }
            else if(tagName.equals(flightX)) {

                flightXPos = Double.parseDouble(m.getTextContent());

            } 
            else if(tagName.equals(flightY)) {

                flightYPos = Double.parseDouble(m.getTextContent());

            } 
            else if(tagName.equals(cardFrontFile) || tagName.equals(cardBackFile)) {
                
                String path = imgPath;
                
                if(Color.RED.equals(cityColor))
                    path += "cityCards/red/";
                else if (Color.GREEN.equals(cityColor))
                    path += "cityCards/green/";
                else if (Color.YELLOW.equals(cityColor))
                    path += "cityCards/yellow/";
                
                Image img = new Image(path + m.getTextContent());
                
                if(tagName.equals(cardFrontFile))
                    cardFront = img;
                else
                    cardBack = img;
                
            }
            else if(tagName.equals(edgesTag)) {

                NodeList edgeList = a.getChildNodes();
                for(int k = 0; k < edgeList.getLength(); k++) {
                    
                    Node n = edgeList.item(k);
                    if(n.getNodeType() != Node.ELEMENT_NODE)
                        continue;
                    
                    String edgeName = null;
                    EdgeType edgeType = null;
                    
                    NodeList edgeAttributes = n.getChildNodes();
                    for(int l = 0; l < edgeAttributes.getLength(); l++){
                        
                        Node att = edgeAttributes.item(l);
                        if(att.getNodeType() != Node.ELEMENT_NODE)
                            continue;
                        
                        switch(att.getNodeName()) {
                            case "name":
                                edgeName = att.getTextContent();
                                break;
                            case "type":
                                try {
                                    edgeType = EdgeType.valueOf(att.getTextContent());
                                } catch (Exception e) {
                                    edgeType = EdgeType.ROAD;
                                }
                                break;
                        }
                        
                    }
                    
                    edges.add(new Edge(edgeName, edgeType));
                   
                }

            } 
            else if(tagName.equals(instructions)) {

                /* IMPLEMENT */

            }

        }
        
        /* Return the new constructed CityNode. */
        CityNode city = new CityNode(cityColor, cityName, cityXPos, cityYPos, quarterVal,  
                flightXPos, flightYPos, cityIsFlight, cardFront, cardBack);
        
        for (Edge e : edges)
            city.addEdge(e);
        
        return city;
        
    }
    
    
}
