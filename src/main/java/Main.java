import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.util.Matrix;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class Main {

static ArrayList<File> allPDFS = new ArrayList<>();

    public static void main(String[] args) {
        JFrame frame = new JFrame("PDF Rotator");
        JPanel panel = new JPanel();
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> pdfList = new JList(model);
        JButton btnUploadFiles = new JButton("PDFs auswählen");
        JButton addPage = new JButton("1 Seite hinzufügen nach der ersten Seite" +
                "");
        addPage.setVisible(false);
        JButton rotatePDF = new JButton("90 grad drehen (gegen uhrzeigersinn)");
        rotatePDF.setVisible(false);
        btnUploadFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser= new JFileChooser("I:\\4_pla\\41_vue");
                chooser.setMultiSelectionEnabled(true);
                chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
                chooser.addChoosableFileFilter(new FileNameExtensionFilter("*.pdf", "pdf"));
                int choice = chooser.showOpenDialog(frame);
                if (choice != JFileChooser.APPROVE_OPTION) return;
                File[] files = chooser.getSelectedFiles();
                allPDFS = new ArrayList<>();
                model.removeAllElements();
                for(int i = 0; i < files.length; i++) {
                    model.addElement(files[i].getName());
                    allPDFS.add(files[i]);
                }
                addPage.setVisible(true);
                rotatePDF.setVisible(true);
                frame.pack();
            }
        });

        addPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for(int i = 0; i < allPDFS.size(); i++) {
                    try {
                        PDDocument document = PDDocument.load(allPDFS.get(i));
                        PDPageTree allPages = document.getDocumentCatalog().getPages();
                        PDPage emptyPage = new PDPage(PDPage.PAGE_SIZE_A4);
                        emptyPage.setRotation(document.getPage(i).getRotation());
                        allPages.insertAfter(emptyPage, document.getPage(0));

                        document.save(allPDFS.get(i).getAbsoluteFile());
                        document.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                JOptionPane.showMessageDialog(frame, allPDFS.size() + " Dokumente wurden je 1 Seite nach der ersten Seite hinzugefügt");
            }
        });

        rotatePDF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for(int i = 0; i < allPDFS.size(); i++) {
                    PDDocument document = null;
                    try {
                        document = PDDocument.load(allPDFS.get(i));

                        for(int ii = 0; ii < document.getNumberOfPages(); ii++) {
                            PDPage page = document.getPage(ii);
                            page.setRotation(page.getRotation() - 90);
                        }

                        document.save(allPDFS.get(i).getAbsoluteFile());
                        document.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                JOptionPane.showMessageDialog(frame, allPDFS.size() + " Dokumente wurden gedreht");
            }
        });

        panel.add(btnUploadFiles);
        panel.add(pdfList);
        panel.add(rotatePDF);
        panel.add(addPage);

        frame.setLocation(100,100);
        frame.setContentPane(panel);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
