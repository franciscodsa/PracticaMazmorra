package mazmorras;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MazmorraGUI {
    private static String currentRoom;
    private static Document document;

    private static Element firstRoom;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        // Crear el marco principal
        JFrame frame = new JFrame("Frame principal");

        // Crear la barra de menú
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Opciones");
        JMenuItem loadMenuItem = new JMenuItem("Load");
        JMenuItem startMenuItem = new JMenuItem("Start");

        fileMenu.add(loadMenuItem);
        fileMenu.add(startMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);


        // Configurar el árbol en el lado izquierdo
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("XML Document");
        JTree tree = new JTree(root);


        // Configurar los botones en el lado derecho
        JButton northButton = new JButton(Constantes.NORTE);
        JButton southButton = new JButton(Constantes.SUR);
        JButton eastButton = new JButton(Constantes.ESTE);
        JButton westButton = new JButton(Constantes.OESTE);

        northButton.setEnabled(false);
        southButton.setEnabled(false);
        eastButton.setEnabled(false);
        westButton.setEnabled(false);

        // Configurar el panel de respuesta en la parte inferior derecha
        JTextArea historialTxt = new JTextArea(5, 35);
        historialTxt.setEditable(false);
        JScrollPane responseScrollPane = new JScrollPane(historialTxt);

        // Crear un contenedor para los botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(northButton, BorderLayout.NORTH);
        buttonPanel.add(southButton, BorderLayout.SOUTH);
        buttonPanel.add(eastButton, BorderLayout.EAST);
        buttonPanel.add(westButton, BorderLayout.WEST);

        JTextArea descripcionTxt = new JTextArea(20, 35);
        descripcionTxt.setWrapStyleWord(true);
        descripcionTxt.setEditable(false);
        descripcionTxt.setLineWrap(true);

        buttonPanel.add(descripcionTxt);


        // Crear un contenedor para el lado derecho
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(buttonPanel, BorderLayout.NORTH);
        rightPanel.add(responseScrollPane, BorderLayout.CENTER);

        // Dividir el contenido en dos paneles
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tree), rightPanel);
        splitPane.setDividerLocation(200);

        // Configurar el marco principal
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(splitPane);
        frame.setSize(800, 600);
        frame.setVisible(true);

        //Muestro mensaje inicial con instrucciones
        historialTxt.append(Constantes.SOLICITAR_XML_Y_START);

        //Agregar acción a opción Load
        loadMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(frame);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    cargarXml(tree, selectedFile);
                    historialTxt.append(Constantes.INSTRUCCION_PRESSIONA_START);
                }
            }
        });

        //Agregar acción a opción Start
        startMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (firstRoom != null) {
                    //Activo botones
                    northButton.setEnabled(true);
                    southButton.setEnabled(true);
                    eastButton.setEnabled(true);
                    westButton.setEnabled(true);

                    // Establecer la primera habitación como la habitación actual
                    currentRoom = firstRoom.getAttribute(Constantes.ID_ROOM_ATTRIBUTE);
                    //Mensaje de bienvenida y descripcion de primera habitacion
                    historialTxt.append(Constantes.MENSAJE_INICIAL);
                    descripcionTxt.setText(getRoomDescription(currentRoom));
                } else {
                    historialTxt.append(Constantes.SOLICITAR_XML_Y_START);
                }
            }
        });

        // Agregar acción al botón Norte
        northButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verificar si se puede ir al norte desde la habitación actual
                if (canGoSelectedDirection(currentRoom, Constantes.NORTE)) {
                    currentRoom = getSelectedDirectionRoom(currentRoom, Constantes.NORTE);
                    historialTxt.append(Constantes.TE_ENCUENTRAS_EN_LA_HABITACION + currentRoom + "\n");
                    descripcionTxt.setText(getRoomDescription(currentRoom));
                } else {
                    historialTxt.append(Constantes.NO_HAY_UNA_PUERTA_HACIA_LA_DIRECCION_SELECCIONADA);
                }
            }
        });

        //Agregar acción al botón Sur
        southButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canGoSelectedDirection(currentRoom, Constantes.SUR)) {
                    currentRoom = getSelectedDirectionRoom(currentRoom, Constantes.SUR);
                    historialTxt.append(Constantes.TE_ENCUENTRAS_EN_LA_HABITACION + currentRoom + "\n");
                    descripcionTxt.setText(getRoomDescription(currentRoom));
                } else {
                    historialTxt.append(Constantes.NO_HAY_UNA_PUERTA_HACIA_LA_DIRECCION_SELECCIONADA);
                }
            }
        });

        //Agregar acción al botón Este
        eastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canGoSelectedDirection(currentRoom, Constantes.ESTE)) {
                    currentRoom = getSelectedDirectionRoom(currentRoom, Constantes.ESTE);
                    historialTxt.append(Constantes.TE_ENCUENTRAS_EN_LA_HABITACION + currentRoom + "\n");
                    descripcionTxt.setText(getRoomDescription(currentRoom));
                } else {
                    historialTxt.append(Constantes.NO_HAY_UNA_PUERTA_HACIA_LA_DIRECCION_SELECCIONADA);
                }
            }
        });

        //Agregar acción al botón Sur
        westButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canGoSelectedDirection(currentRoom, Constantes.OESTE)) {
                    currentRoom = getSelectedDirectionRoom(currentRoom, Constantes.OESTE);
                    historialTxt.append(Constantes.TE_ENCUENTRAS_EN_LA_HABITACION + currentRoom + "\n");
                    descripcionTxt.setText(getRoomDescription(currentRoom));
                } else {
                    historialTxt.append(Constantes.NO_HAY_UNA_PUERTA_HACIA_LA_DIRECCION_SELECCIONADA);
                }
            }
        });
    }

    private static boolean canGoSelectedDirection(String room, String direction) {
        // Obtener el elemento "room" correspondiente a la habitación actual
        Element currentRoomElement = getRoomElement(room);

        if (currentRoomElement != null) {
            // Verificar si hay una puerta al norte en la habitación actual
            NodeList doors = currentRoomElement.getElementsByTagName(Constantes.DOOR_XML_TAG);
            for (int i = 0; i < doors.getLength(); i++) {
                Element doorElement = (Element) doors.item(i);
                String doorName = doorElement.getAttribute(Constantes.NAME_DOOR_ATTRIBUTE);
                if (direction.equals(doorName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String getSelectedDirectionRoom(String room, String direction) {
        Element currentRoomElement = getRoomElement(room);

        if (currentRoomElement != null) {
            // Obtener la habitación de destino al norte desde la habitación actual
            NodeList doors = currentRoomElement.getElementsByTagName(Constantes.DOOR_XML_TAG);
            for (int i = 0; i < doors.getLength(); i++) {
                Element doorElement = (Element) doors.item(i);
                String doorName = doorElement.getAttribute(Constantes.NAME_DOOR_ATTRIBUTE);
                if (direction.equals(doorName)) {
                    return doorElement.getAttribute(Constantes.DEST_DOOR_ATTRIBUTE);
                }
            }
        }
        return "";
    }

    private static String getRoomDescription(String room) {
        // Obtener el elemento "room" correspondiente a la habitación
        Element roomElement = getRoomElement(room);

        if (roomElement != null) {
            // Obtener la descripción de la habitación
            NodeList descriptionNodes = roomElement.getElementsByTagName(Constantes.DESCRIPTION_XML_TAG);
            if (descriptionNodes.getLength() > 0) {
                return descriptionNodes.item(0).getTextContent();
            }
        }
        return Constantes.DESCRIPCION_NO_DISPONIBLE;
    }

    private static Element getRoomElement(String room) {
        // Buscar el elemento "room" con el atributo "id" igual al nombre de la habitación
        NodeList roomNodes = document.getElementsByTagName(Constantes.ROOM_XML_TAG);
        for (int i = 0; i < roomNodes.getLength(); i++) {
            Element roomElement = (Element) roomNodes.item(i);
            if (room.equals(roomElement.getAttribute(Constantes.ID_ROOM_ATTRIBUTE))) {
                return roomElement;
            }
        }
        return null;
    }

    private static void cargarXml(JTree tree, File selectedFile) {
        // Cargar el archivo XML y mostrarlo en el árbol
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(selectedFile);

            // Obtener el primer elemento "room" del XML
            firstRoom = getFirstRoomElement(document);

            Element rootElement = document.getDocumentElement();

            DefaultMutableTreeNode rootNode = new CustomTreeNode(rootElement);
            tree.setModel(new DefaultTreeModel(buildTree(rootElement)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Element getFirstRoomElement(Document document) {
        NodeList roomNodes = document.getElementsByTagName(Constantes.ROOM_XML_TAG);
        if (roomNodes.getLength() > 0) {
            return (Element) roomNodes.item(0); // Devuelve el primer elemento "room" encontrado
        }
        return null;
    }

    private static CustomTreeNode buildTree(Element element) {
        CustomTreeNode treeNode = new CustomTreeNode(element);
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) {
                CustomTreeNode childNode = buildTree((Element) children.item(i));
                treeNode.add(childNode);
            }
        }
        return treeNode;
    }
}