package multiutils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.BadLocationException;

public class Form extends JFrame {

    private String textForSearch = "";
    private JTable t;

    public Form() {
        DefaultTableModel model;
        t = new JTable(
        
        		model = new DefaultTableModel(new Object[][]{},new Object[]{1}));
        for(int i =0;i<10;i++){
            model.addRow(new Object[]{i+"abd"});
        }
        for(int i =0;i<t.getColumnCount();i++){
            t.getColumnModel().getColumn(i).setCellRenderer(getRenderer());
        }

        JScrollPane jsp = new JScrollPane(t);
        final RightPanel right = new RightPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(jsp, BorderLayout.CENTER);
        add(right, BorderLayout.EAST);
        pack();
        setLocationRelativeTo(null);
    }

    private TableCellRenderer getRenderer() {
        return new TableCellRenderer() {
            JTextField f = new JTextField();

            @Override
            public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
                if(arg1 != null){
                    f.setText(arg1.toString());
                    String string = arg1.toString();
                    if(string.contains(textForSearch)){
                        int indexOf = string.indexOf(textForSearch);
                        try {
                            f.getHighlighter().addHighlight(indexOf,indexOf+textForSearch.length(),new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.RED));
                        } catch (BadLocationException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    f.setText("");
                    f.getHighlighter().removeAllHighlights();
                }
                return f;
            }
        };
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                new Form().setVisible(true);
            }
        });
    }

    class RightPanel extends JPanel{


        public RightPanel(){
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(5, 5, 5, 5);
            c.gridy = 0;
            final JTextField f = new JTextField(5);
            add(f,c);
            JButton b = new JButton("search");
            b.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    textForSearch = f.getText();
                    t.repaint();
                }
            });
            c.gridy++;
            add(b,c);
        }
    }

}
