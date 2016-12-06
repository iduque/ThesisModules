/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewJFrame.java
 *
 * Created on 13-Mar-2013, 14:58:52
 */
package View;

import Main.CommonVar;
import Main.Controller;
import Model.Answer;
import Model.Question;
import Model.Questionnaire;
import XML_IO.QuestionnaireXMLWriter;
import Model.Questionnaires;
import XML_IO.QuestionnaireToCSV;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author id11ab
 */
public final class Interface extends javax.swing.JFrame {
    
    private Controller _controller;
    
    private Questionnaires _questionnaires;
    private Questionnaire _preferencesQuestionnaire;
    private Questionnaire _questionnaire;
    
    private String _currentMode;
    
    public Interface (Controller c){
        _controller = c;
        initComponents();
    }
    
    /** Creates new form Interface */
    public void showInterface(Questionnaires qs, Questionnaire q, Questionnaire prefQ, String mode) {
        
        
        //Set all the components depending on the mode
        if (mode.equals(CommonVar.ADMIN)){
            
            _jlModeLabel.setText(CommonVar.ADMIN + " Mode");
            _jlUserName.setText("");
            _jbSubmit.setText("Test");
            
            _jspAdmin.setVisible(true);
            _jspUser.setVisible(false);
            
            _questionnaires = qs;
            fillTableAdmin(qs);
            
        }else if (mode.equals(CommonVar.USER)){
            
            _jlModeLabel.setText(CommonVar.USER + ":");
            _jlUserName.setText(q.getUser());
            _jbSubmit.setText("Submit");
            
            _jspUser.setVisible(true);
            _jspAdmin.setVisible(false);
            
            _questionnaire = q;
            initializeQuestionnaire(_questionnaire,false, _jpQuestions);
            
            _preferencesQuestionnaire = prefQ;
            initializeQuestionnaire(_preferencesQuestionnaire, false, _jpPreferences);
        }
        
        _currentMode = mode;
        
        //Show the window
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    
    /**
     * Initializing the questionnarie
     * @param questionnaire List of questions
     * @param subquestion True when a question has any subquestion
     */ 
   public void initializeQuestionnaire(Questionnaire questionnarie, boolean subquestion, JPanel panel){
       
       
       int cont = 1;
       for( Map.Entry<Integer, Question> entry : questionnarie.getAllQuestions().entrySet()){
           Question q = entry.getValue();
           JLabel label = new JLabel(formatString(800, q.getTitle(), cont));
           
           if (subquestion){
                label.setName("S" + q.getId());
                label.setBorder(new EmptyBorder(new Insets(10, 15, 0, 0)));
                label.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 16));
           }else{
               label.setName("Q" + q.getId());
               label.setBorder(new EmptyBorder(new Insets(10, 5, 0, 0)));
               label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
           }
           panel.add(label);

           //If the question has a set of answers
           if (!q.getQuestionAnswers().isEmpty()){
               JPanel container = new JPanel();
               container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));
               container.setAlignmentX(0);

               ButtonGroup bg = new ButtonGroup();
               for (Answer a : q.getQuestionAnswers()){

                    if(q.getType().equals(CommonVar.TYPE_MULTIPLECHOICE)){
                        JCheckBox cb = new JCheckBox();
                        cb.setForeground(Color.BLUE); cb.setMargin(new Insets(10, 25, 10, 0));
                        cb.setName("C" + a.getIntValue());
                        cb.setSelected(a.isSelected());

                        if(a.isTextInput()){
                            //Set the CheckBox text. Take string before ':' - "Other: Text" -> "Other"
                            cb.setText((a.getText().split(":")[0] + ":"));

                            JTextField tf =  new JTextField();
                            //Set the TextField text. All text after symbol ':'
                            if(a.getText().split(":").length > 1)
                                tf.setText(a.getText().split(":")[1]); //Take string after ':' - "Other: Text" -> " Text"
                            else tf.setText("");
                            tf.setMaximumSize(new Dimension(200, 20));
                            tf.setName("T" + a.getIntValue());
                            container.add(cb); //Add the Radio Button to the panel
                            container.add(tf);
                        }else{
                            cb.setText(a.getText());
                            container.add(cb);//Add the Radio Button to the panel
                        } 

                    }else if(q.getType().equals(CommonVar.TYPE_SINGLECHOICE)){
                        JRadioButton rb = new JRadioButton();
                        rb.setForeground(Color.BLUE); rb.setMargin(new Insets(10, 25, 10, 0));
                        rb.setName("R" + a.getIntValue());
                        rb.setSelected(a.isSelected());

                        if(a.isTextInput()){
                            //Set the CheckBox text. Take string before ':' - "Other: Text" -> "Other"
                            rb.setText((a.getText().split(":")[0] + ":"));

                            JTextField tf =  new JTextField();
                            //Set the TextField text. All text after symbol ':'
                            if(a.getText().split(":").length > 1)
                                tf.setText(a.getText().split(":")[1]); //Take string after ':' - "Other: Text" -> " Text"
                            else tf.setText("");
                            tf.setMaximumSize(new Dimension(200, 20));
                            tf.setName("T" + a.getIntValue());
                            container.add(rb); //Add the Radio Button to the panel
                            container.add(tf);
                        }else{
                            rb.setText(a.getText());
                            container.add(rb);//Add the Radio Button to the panel
                        }

                        bg.add(rb); //Add the Radio button ton the ButtonGroup

                    }else if(q.getType().equals(CommonVar.TYPE_TEXT)){
                        JRadioButton rb = new JRadioButton("", Boolean.TRUE);
                        rb.setForeground(Color.BLUE); rb.setMargin(new Insets(10, 25, 10, 0));
                        rb.setName("R" + a.getIntValue());
                        container.add(rb); //Add the Radio Button to the panel
                        
                        JTextField tf =  new JTextField();
                        tf.setText(a.getText());
                        tf.setMaximumSize(new Dimension(400, 20));
                        tf.setName("T" + a.getIntValue());
                        container.add(tf);//Add the TextInput field to the panel
                    }
                }
                panel.add(container);
           
           //If the question has a subquestion
           }else if(q.getSubquestions().getNumberQuestions() > 0)
                initializeQuestionnaire(q.getSubquestions(), true, panel);

           //Introduce a JSepartor for each question, no subquestion
           if (!subquestion){
                JSeparator js = new JSeparator(SwingConstants.HORIZONTAL);
                js.setName("JS");
                js.setBorder(new EmptyBorder(100, 10, 10, 10)); 
                panel.add(js);
           }
           
           cont++;
           
       }
       this._jspUser.getVerticalScrollBar().setUnitIncrement(20);
       panel.validate();
       
  }
   
   /**
    * Fill the questionnaires list table
    * @param qs List of questionnaires names to be shown
    */
   private void fillTableAdmin(Questionnaires qs){

       for(Entry<String, Questionnaire> e: qs.getAllQuestions().entrySet()){
           //If the user is not already in the table
           if(!((TableModel) _jtQuestionnairesList.getModel()).isUser(e.getKey()))
                  ((TableModel) _jtQuestionnairesList.getModel()).
                          addRow(Arrays.asList(e.getKey(), "Questionnaire-" + e.getKey(), false));
       }
   }
   
   private String formatString(int width, String input, int cont){ 
       return "<html><body style='width:" + width + "px'> " + cont + ". " + input + "</body></html>";  
   }
   
   private String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected())
                return button.getText();
        }
        return null;
   }

   /**
    * Check the validity of the questionnaire and store the selected value on the object questionnaire
    * @return Boolean If the questionnaire is valid
    */
   private Boolean checkQuestionnaire(JPanel jp, Questionnaire questionnaire){

       int questionCounter = 0;
       int subquestionCounter = 0;

       //For each component of the questionnaire
       for(Component c: jp.getComponents()){         

          //JLabel = Question (Q) or Subquestion(S)
         if (c.getClass().getName().equals("javax.swing.JLabel")){
             
             if (c.getName().startsWith("Q")) questionCounter++;
             else if (c.getName().startsWith("S")) subquestionCounter++;

         //JPanel answers for a question or subquestion. They have to be selected when required
         }else if(c.getClass().getName().equals("javax.swing.JPanel")){
             
            boolean selected = false;

            //For each component inside the JPanel (JRadioButton or JTextField)
            for(Component c1: ((JPanel) c).getComponents()){

               if(c1.getClass().getName().equals("javax.swing.JRadioButton")){
               
                   if (((JRadioButton) c1).isSelected()){
                           setAnswerValue(questionnaire, subquestionCounter, questionCounter, c1, Boolean.TRUE);
                           selected = true;        
                   }else{
                       setAnswerValue(questionnaire, subquestionCounter, questionCounter, c1, Boolean.FALSE);
                       if(!questionnaire.getQuestionById(questionCounter).isRequired()) selected = true;
                   }

               }else if(c1.getClass().getName().equals("javax.swing.JTextField")){
                   Answer a;
                   if(subquestionCounter == 0)
                          a = questionnaire.getQuestionById(questionCounter).getAnswerByValue(
                                   Integer.valueOf(c1.getName().substring(1)));
                   else
                      a = questionnaire.getQuestionById(questionCounter).getSubquestionById(subquestionCounter).
                              getAnswerByValue(Integer.valueOf(c1.getName().substring(1)));
                   
                   //If the text field is associated with a radiobutton, we have to keep previous text (before ':')
                  if(a.isTextInput() && a.isSelected())
                      a.setText(a.getText().split(":")[0] + ":" + ((JTextField) c1).getText());
                  else if (!a.isTextInput())  a.setText(((JTextField) c1).getText());
                   
               } else if(c1.getClass().getName().equals("javax.swing.JCheckBox")){
                   
                   if (((JCheckBox) c1).isSelected()){
                           setAnswerValue(questionnaire, subquestionCounter, questionCounter, c1, Boolean.TRUE);
                           selected = true;        
                   }else{
                       setAnswerValue(questionnaire, subquestionCounter, questionCounter, c1, Boolean.FALSE);
                       if(!questionnaire.getQuestionById(questionCounter).isRequired()) selected = true;
                   }
               }
            }
            if (!selected){
                JOptionPane.showMessageDialog(null, "Upss!! You forgot to fill in Question " + questionCounter);
                return Boolean.FALSE;
            } 
         }else if(c.getClass().getName().equals("javax.swing.JSeparator")){
            //Each JSeparator indicates a new question, so the subquestion counter has to be restarted
            subquestionCounter = 0;
         }
       }

       return Boolean.TRUE;
   }
   
   //Set the selected value of one answer in the questionnaire
   private void setAnswerValue(Questionnaire questionnaire, int subquestionCounter, int questionCounter, Component c1, Boolean value){
       
       if(subquestionCounter == 0){
           questionnaire.getQuestionById(questionCounter).getAnswerByValue(
                   Integer.valueOf(c1.getName().substring(1))).setSelected(value);
       }else{
          questionnaire.getQuestionById(questionCounter).getSubquestionById(subquestionCounter).getAnswerByValue(
                   Integer.valueOf(c1.getName().substring(1))).setSelected(value);
       }
   }
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        _jpHeader = new javax.swing.JPanel();
        _jlUserName = new javax.swing.JLabel();
        _jlModeLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        _jpCentral = new javax.swing.JPanel();
        _jspUser = new javax.swing.JScrollPane();
        _jpQuestions = new javax.swing.JPanel();
        _jspAdmin = new javax.swing.JScrollPane();
        _jtQuestionnairesList = new javax.swing.JTable();
        _jspPreferences = new javax.swing.JScrollPane();
        _jpPreferences = new javax.swing.JPanel();
        _jbSubmit = new javax.swing.JButton();
        _jbBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Questionnaire Aplication");
        setMinimumSize(new java.awt.Dimension(1150, 0));

        _jlModeLabel.setText("User: ");

        jLabel2.setFont(new java.awt.Font("Bookman Old Style", 3, 24));
        jLabel2.setText("Robot House Questionnaires Application");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/University of Hertfordshire.gif"))); // NOI18N

        javax.swing.GroupLayout _jpHeaderLayout = new javax.swing.GroupLayout(_jpHeader);
        _jpHeader.setLayout(_jpHeaderLayout);
        _jpHeaderLayout.setHorizontalGroup(
            _jpHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(_jpHeaderLayout.createSequentialGroup()
                .addGroup(_jpHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(_jpHeaderLayout.createSequentialGroup()
                        .addComponent(_jlModeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_jlUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 312, Short.MAX_VALUE)
                .addComponent(jLabel1))
        );
        _jpHeaderLayout.setVerticalGroup(
            _jpHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(_jpHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(_jpHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(_jpHeaderLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(_jpHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(_jlUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(_jlModeLabel))))
                .addGap(12, 12, 12))
        );

        _jpQuestions.setLayout(new javax.swing.BoxLayout(_jpQuestions, javax.swing.BoxLayout.Y_AXIS));
        _jspUser.setViewportView(_jpQuestions);

        _jtQuestionnairesList.setModel(new TableModel());
        _jspAdmin.setViewportView(_jtQuestionnairesList);

        _jpPreferences.setLayout(new javax.swing.BoxLayout(_jpPreferences, javax.swing.BoxLayout.Y_AXIS));
        _jspPreferences.setViewportView(_jpPreferences);

        javax.swing.GroupLayout _jpCentralLayout = new javax.swing.GroupLayout(_jpCentral);
        _jpCentral.setLayout(_jpCentralLayout);
        _jpCentralLayout.setHorizontalGroup(
            _jpCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1103, Short.MAX_VALUE)
            .addGroup(_jpCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(_jpCentralLayout.createSequentialGroup()
                    .addComponent(_jspAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 1103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(_jpCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(_jpCentralLayout.createSequentialGroup()
                    .addComponent(_jspUser, javax.swing.GroupLayout.PREFERRED_SIZE, 1102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(_jpCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(_jpCentralLayout.createSequentialGroup()
                    .addComponent(_jspPreferences, javax.swing.GroupLayout.DEFAULT_SIZE, 1103, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        _jpCentralLayout.setVerticalGroup(
            _jpCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 520, Short.MAX_VALUE)
            .addGroup(_jpCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(_jpCentralLayout.createSequentialGroup()
                    .addComponent(_jspAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(_jpCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(_jpCentralLayout.createSequentialGroup()
                    .addComponent(_jspUser, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(17, Short.MAX_VALUE)))
            .addGroup(_jpCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(_jpCentralLayout.createSequentialGroup()
                    .addComponent(_jspPreferences, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        _jbSubmit.setFont(new java.awt.Font("Tahoma", 1, 14));
        _jbSubmit.setText("Submit");
        _jbSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _jbSubmitActionPerformed(evt);
            }
        });

        _jbBack.setFont(new java.awt.Font("Tahoma", 1, 14));
        _jbBack.setText("Back");
        _jbBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _jbBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(_jbBack, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 853, Short.MAX_VALUE)
                        .addComponent(_jbSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(_jpCentral, 0, 1103, Short.MAX_VALUE)
                    .addComponent(_jpHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(_jpHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_jpCentral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_jbBack, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(_jbSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void _jbSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__jbSubmitActionPerformed
    
    if(_currentMode.equals(CommonVar.USER)){
        if (checkQuestionnaire(this._jpQuestions, _questionnaire)){
            //Write the questionnaire to a file
            QuestionnaireXMLWriter.writeXML("Users\\Questionnaire-" + 
                    _questionnaire.getUser() + ".xml", _questionnaire, false);
            System.out.println("Questionnaire Recorded");
            this._jpQuestions.removeAll();

            //Update the database with the user preferences and the assigned persona
            System.out.println("Storing user preferences in DB and maching user with Persona");
            _controller.setUserDB(_preferencesQuestionnaire, _questionnaire);

            //Close the windows and return to the initial view
            _controller.setScreen(CommonVar.INITIAL_VIEW, "", "", "");
            this.setVisible(false);
                       
            //Show the preferences form
//            _currentMode = CommonVar.PREFERENCES;
//            _jspUser.setVisible(false);
//            _jspPreferences.setVisible(true);
//            _jbSubmit.setText("Submit");
            
        }
//        else
//        { //To be removed, just for quick access to preferences form
//            //Show the preferences form
//            _currentMode = CommonVar.PREFERENCES;
//            _jspUser.setVisible(false);
//            _jspPreferences.setVisible(true);
//            _jbSubmit.setText("Submit");
//        }
    }else if (_currentMode.equals(CommonVar.ADMIN)){
        
        List<Questionnaire> selectedQuestionnaries = new ArrayList<Questionnaire>();

        for(int i=0; i<((TableModel) _jtQuestionnairesList.getModel()).getRowCount(); i++){
            Boolean isSelected = (Boolean) ((TableModel) _jtQuestionnairesList.getModel()).getValueAt(i, 2);
            
            if(isSelected){
                String user = (String) ((TableModel) _jtQuestionnairesList.getModel()).getValueAt(i, 0);
                _controller.matchPersona(_questionnaires.getQuestionnaire(user));
                selectedQuestionnaries.add(_questionnaires.getQuestionnaire(user));
            }
            
        }

        QuestionnaireToCSV.writeCSV("Results-E2", selectedQuestionnaries);
        
    }else if(_currentMode.equals(CommonVar.PREFERENCES)){
        
        if (checkQuestionnaire(this._jpPreferences, _preferencesQuestionnaire)){
            //Write the preferences questionnaire to a file
            QuestionnaireXMLWriter.writeXML("Preferences\\PreferencesQuestionnaire-" +
                    _preferencesQuestionnaire.getUser() + ".xml", _preferencesQuestionnaire, true);
            System.out.println("Preferences Questionnaire Recorded");
            this._jpPreferences.removeAll();
            
            //Update the database with the user preferences and the assigned persona
            System.out.println("Storing user preferences in DB and maching user with Persona");
            _controller.setUserDB(_preferencesQuestionnaire, _questionnaire);   
        
            //Close the windows and return to the initial view
            _controller.setScreen(CommonVar.INITIAL_VIEW, "", "", "");
            this.setVisible(false);
        }
                
    }
    
}//GEN-LAST:event__jbSubmitActionPerformed

private void _jbBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__jbBackActionPerformed
        _controller.setScreen(CommonVar.INITIAL_VIEW, "", "", "");
        this.setVisible(false);
}//GEN-LAST:event__jbBackActionPerformed

  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton _jbBack;
    private javax.swing.JButton _jbSubmit;
    private javax.swing.JLabel _jlModeLabel;
    private javax.swing.JLabel _jlUserName;
    private javax.swing.JPanel _jpCentral;
    private javax.swing.JPanel _jpHeader;
    private javax.swing.JPanel _jpPreferences;
    private javax.swing.JPanel _jpQuestions;
    private javax.swing.JScrollPane _jspAdmin;
    private javax.swing.JScrollPane _jspPreferences;
    private javax.swing.JScrollPane _jspUser;
    private javax.swing.JTable _jtQuestionnairesList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
