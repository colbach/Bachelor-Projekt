package view.dialogs.codeinput;

import java.awt.Point;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import reflection.customdatatypes.SourceCode;
import utils.codecompilation.CodeCompiler;
import utils.codecompilation.CompilationError;
import utils.format.TimeFormat;
import utils.structures.FutureValue;

public class CodeInputDialog extends javax.swing.JDialog {

    public static SourceCode showSourceCodeInputDialog(CodeInputInstructions codeInputInstructions, java.awt.Frame parent) {

        CodeInputDialog codeInputDialog = new CodeInputDialog(codeInputInstructions, parent);
        codeInputDialog.setVisible(true);

        return codeInputDialog.sourceCode;
    }

    private final CodeCompiler compiler;
    private final CodeInputInstructions codeInputInstructions;
    private SourceCode sourceCode;

    /**
     * Creates new form CodeInputDialog
     */
    public CodeInputDialog(CodeInputInstructions codeInputInstructions, java.awt.Frame parent) {
        super(parent, true);
        initComponents();

        this.codeInputInstructions = codeInputInstructions;
        this.compiler = CodeCompiler.getInstance();
        String template = codeInputInstructions.getTemplate();
        if (template != null) {
            codeTextArea.setText(template);
        }
        if (getCode().length() == 0) {
            compileButton.setEnabled(false);
        }
        testButton.setEnabled(false);
        if (!codeInputInstructions.isTestable()) {
            testButton.setVisible(false);
        }
        codeTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                compileButton.setEnabled(true);
                testButton.setEnabled(false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                compileButton.setEnabled(true);
                testButton.setEnabled(false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                compileButton.setEnabled(true);
                testButton.setEnabled(false);
            }
        });
        
        Point parentLocation = parent.getLocation();
        
        double parentX = parentLocation.getX();
        double parentY = parentLocation.getY();
        double parentCenterX = parentX + parent.getWidth() / 2;
        double parentCenterY = parentY + parent.getHeight() / 2;
        setLocation((int)(parentCenterX - getWidth() / 2), (int)(parentCenterY - getHeight() / 2));
    }

    public String getCode() {
        return codeTextArea.getText();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        codeTextArea = new javax.swing.JTextArea();
        okButton = new javax.swing.JButton();
        abordButton = new javax.swing.JButton();
        testButton = new javax.swing.JButton();
        compileButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Java-Code-Editor");

        codeTextArea.setColumns(20);
        codeTextArea.setFont(new java.awt.Font("Courier New", 0, 16)); // NOI18N
        codeTextArea.setRows(5);
        codeTextArea.setTabSize(3);
        jScrollPane1.setViewportView(codeTextArea);

        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        abordButton.setText("Abbruch");
        abordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abordButtonActionPerformed(evt);
            }
        });

        testButton.setText("Testen");
        testButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testButtonActionPerformed(evt);
            }
        });

        compileButton.setText("Kompilieren");
        compileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compileButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 721, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(compileButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(testButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(abordButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okButton))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(abordButton)
                    .addComponent(testButton)
                    .addComponent(compileButton)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void compileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compileButtonActionPerformed

        try {
            long time = System.currentTimeMillis();
            compiler.compileAndLoadClass(getCode());
            compileButton.setEnabled(false);
            testButton.setEnabled(true);
            JOptionPane.showMessageDialog(this,
                    "Erfolgreich kompiliert (" + TimeFormat.format(System.currentTimeMillis() - time) + ", evt. gecached Ergebnis)");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.toString(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_compileButtonActionPerformed

    private void testButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testButtonActionPerformed
        try {
            Object newInstance = compiler.compileAndCreateNewInstance(getCode());
            CodeInputTestResult testResult = codeInputInstructions.performTest(newInstance);
            if (testResult.isPassed()) {
                JOptionPane.showMessageDialog(this,
                        "Test erfolgreich: " + testResult.getMessage(),
                        "Erfolg",
                        JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Test fehlgeschlagen: " + testResult.getMessage(),
                        "Fehler",
                        JOptionPane.PLAIN_MESSAGE);
            }

        } catch (IOException | CompilationError ex) {
            JOptionPane.showMessageDialog(this,
                    ex.toString(),
                    "Test fehlgeschlagen",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_testButtonActionPerformed

    private void abordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abordButtonActionPerformed
        dispose();
    }//GEN-LAST:event_abordButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        sourceCode = new SourceCode(getCode());
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton abordButton;
    private javax.swing.JTextArea codeTextArea;
    private javax.swing.JButton compileButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okButton;
    private javax.swing.JButton testButton;
    // End of variables declaration//GEN-END:variables
}
