package mazmorras;

import org.w3c.dom.Element;

import javax.swing.tree.DefaultMutableTreeNode;

public class CustomTreeNode extends DefaultMutableTreeNode {
    public CustomTreeNode(Object userObject) {
        super(userObject);
    }

    @Override
    public String toString() {
        if (userObject instanceof Element) {
            Element element = (Element) userObject;
            switch (element.getNodeName()) {
                case Constantes.ROOM_XML_TAG -> {
                    return "Room " + element.getAttribute(Constantes.ID_ROOM_ATTRIBUTE);
                }
                case Constantes.DOOR_XML_TAG -> {
                    return "Door: " + element.getAttribute(Constantes.NAME_DOOR_ATTRIBUTE) + " -> " + element.getAttribute(Constantes.DEST_DOOR_ATTRIBUTE);
                }
                case Constantes.DESCRIPTION_XML_TAG -> {
                    return element.getTextContent();
                }
            }
        }
        return super.toString();
    }
}
