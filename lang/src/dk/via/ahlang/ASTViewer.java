package dk.via.ahlang;

import dk.via.ahlang.ast.AST;
import dk.via.ahlang.ast.Terminal;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class ASTViewer extends JFrame {
    private static final Font NODE_FONT = new Font("Verdana", Font.PLAIN, 24);

    public ASTViewer(AST ast) {
        super("Le ahhhh language se very nice");

        DefaultMutableTreeNode root = createTree(ast);
        JTree tree = new JTree(root);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setFont(NODE_FONT);
        tree.setCellRenderer(renderer);

        add(new JScrollPane(tree));

        setSize(1024, 768);
        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private DefaultMutableTreeNode createTree(AST ast) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("yea boiiii");

        if (ast == null) {
            node.setUserObject("OHHH NO");
        } else {
            node.setUserObject(ast.getClass().getSimpleName());
            addFields(node, ast);
        }

        return node;
    }

    private void addFields(DefaultMutableTreeNode node, AST ast) {
        Field[] fields = ast.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(ast);
                if (value != null) {
                    if(value instanceof Terminal) {
                        Terminal t = (Terminal) value;
                        node.add(new DefaultMutableTreeNode(t.getClass().getSimpleName() + "/" + field.getName() + " : " + t.spelling));
                    } else if (value instanceof AST) {
                        node.add(createTree((AST) value));
                    } else if (value instanceof List) {
                        DefaultMutableTreeNode fieldNode = new DefaultMutableTreeNode(field.getName());
                        node.add(fieldNode);
                        for (Object item : (List<?>) value) {
                            if (item instanceof Terminal) {
                                Terminal t = (Terminal) item;
                                fieldNode.add(new DefaultMutableTreeNode(t.getClass().getSimpleName() + "/" + field.getName() + " : " + t.spelling));
                            } else if (item instanceof AST) {
                                fieldNode.add(createTree((AST) item));
                            } else {
                                fieldNode.add(new DefaultMutableTreeNode(item.toString()));
                            }
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
