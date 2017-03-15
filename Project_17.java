
import javax.swing.*;
import javax.swing.undo.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.Date;
import javax.swing.text.*;
import java.text.*;


class Myeditor extends JFrame implements ActionListener
{
	public static Myeditor e;
	
	
	
	JTextArea text = new JTextArea(0,0);
	JScrollPane scroll = new JScrollPane(text);

	JMenuBar mb=new JMenuBar();
	
	JMenu FILE = new JMenu("File");
	JMenu EDIT = new JMenu("Edit");
	JMenu SEARCH = new JMenu("Search");
	JMenu HELP = new JMenu("Help");

	JMenuItem NEWFILE = new JMenuItem("New");
	JMenuItem OPENFILE = new JMenuItem("Open...");
	JMenuItem SAVEFILE = new JMenuItem("Save");
	JMenuItem SAVEASFILE = new JMenuItem("Save As...");
	JMenuItem EXITFILE = new JMenuItem("Exit");


	JMenuItem CUTEDIT = new JMenuItem("Cut");	
	JMenuItem COPYEDIT = new JMenuItem("Copy");
	JMenuItem PASTEDIT = new JMenuItem("Paste");
	JMenuItem DELETEDIT = new JMenuItem("Delete");
	JMenuItem SELECTEDIT = new JMenuItem("Select All");
	JMenuItem TIMEDIT = new JMenuItem("Time/Date");
	JCheckBoxMenuItem WORDEDIT = new JCheckBoxMenuItem("Word Wrap");
	JMenuItem FONTEDIT = new JMenuItem("Set Font...");

	JMenuItem FINDSEARCH = new JMenuItem("Find");
	JMenuItem FINDNEXTSEARCH = new JMenuItem("Find Next");

	JMenuItem ABOUTHELP = new JMenuItem("About");

	JPopupMenu POPUP = new JPopupMenu();
	JMenuItem CUTPOPUP = new JMenuItem("Cut");
	JMenuItem COPYPOPUP = new JMenuItem("Copy");
	JMenuItem PASTEPOPUP = new JMenuItem("Paste");
	JMenuItem DELETEPOPUP = new JMenuItem("Delete");
	JMenuItem SELECTPOPUP = new JMenuItem("Select All");
	

	UndoManager undo = new UndoManager();
	UndoAction undoAction = new UndoAction();
	
	boolean opened = false;
	String wholeText,findString,filename = null;
	int ind = 0;
	
	
	class UndoAction extends AbstractAction 
	{
		public UndoAction() 
		{
	    		super("Undo");
		}

		public void actionPerformed(ActionEvent e) 	
		{
	    		try 
	    		{
				undo.undo();
	    		} 
	    		catch (CannotUndoException ex) 
	    		{
				System.out.println("Unable to undo: " + ex);
				ex.printStackTrace();
	    		}
	    		update();
		}

		protected void update() 
		{
	    		if(undo.canUndo()) 
	    		{
				setEnabled(true);
				putValue("Undo", undo.getUndoPresentationName());
	    		}
	    		else 
	    		{
				setEnabled(false);
				putValue(Action.NAME, "Undo");
	    		}
		}
	}	
	
			
	public Myeditor()
	{
		
		setTitle("Untitled");	
		setSize(600,400);
		setVisible(true);
                                setLocationRelativeTo(null);

		text.setLineWrap(true);
		
		WORDEDIT.setState(true);
		
		getContentPane().setLayout(new BorderLayout());
		
		getContentPane().add(scroll, BorderLayout.CENTER);
		
		setJMenuBar(mb);
		 
		mb.add(FILE);
		mb.add(EDIT);
		mb.add(SEARCH);
		mb.add(HELP);	
		
		
		FILE.add(NEWFILE);
		FILE.add(OPENFILE);
		FILE.add(SAVEFILE);
		FILE.add(SAVEASFILE);
		FILE.addSeparator();
		FILE.add(EXITFILE);
	
		
		EDIT.add(undoAction);
		EDIT.add(CUTEDIT);
		EDIT.add(COPYEDIT);
		EDIT.add(PASTEDIT);
		EDIT.add(DELETEDIT);
		EDIT.addSeparator();
		EDIT.add(SELECTEDIT);
		EDIT.add(TIMEDIT);
		EDIT.addSeparator();
		EDIT.add(WORDEDIT);
		EDIT.add(FONTEDIT);
		
		SEARCH.add(FINDSEARCH);
		SEARCH.add(FINDNEXTSEARCH);

		HELP.add(ABOUTHELP);
				
		
		POPUP.add(undoAction);
		POPUP.addSeparator();
		POPUP.add(CUTPOPUP);
		POPUP.add(COPYPOPUP);
		POPUP.add(PASTEPOPUP);
		POPUP.add(DELETEPOPUP);
		POPUP.addSeparator();
		POPUP.add(SELECTPOPUP);
		
		FILE.setMnemonic(KeyEvent.VK_F);
		EDIT.setMnemonic(KeyEvent.VK_E);
		SEARCH.setMnemonic(KeyEvent.VK_S);
		HELP.setMnemonic(KeyEvent.VK_H);
	
		NEWFILE.setMnemonic(KeyEvent.VK_N);
		OPENFILE.setMnemonic(KeyEvent.VK_O);
		SAVEFILE.setMnemonic(KeyEvent.VK_S);
		SAVEASFILE.setMnemonic(KeyEvent.VK_A);	
		EXITFILE.setMnemonic(KeyEvent.VK_X);	

		
		CUTEDIT.setMnemonic(KeyEvent.VK_T);
		COPYEDIT.setMnemonic(KeyEvent.VK_C);
		PASTEDIT.setMnemonic(KeyEvent.VK_P);
		DELETEDIT.setMnemonic(KeyEvent.VK_L);
		SELECTEDIT.setMnemonic(KeyEvent.VK_A);
		TIMEDIT.setMnemonic(KeyEvent.VK_D);
		WORDEDIT.setMnemonic(KeyEvent.VK_W);
		FONTEDIT.setMnemonic(KeyEvent.VK_F);
		
		
		FINDSEARCH.setMnemonic(KeyEvent.VK_F);
		FINDNEXTSEARCH.setMnemonic(KeyEvent.VK_N);

		ABOUTHELP.setMnemonic(KeyEvent.VK_A);
		
		
		CUTPOPUP.setMnemonic(KeyEvent.VK_T);
		COPYPOPUP.setMnemonic(KeyEvent.VK_C);
		PASTEPOPUP.setMnemonic(KeyEvent.VK_P);
		DELETEPOPUP.setMnemonic(KeyEvent.VK_D);
		SELECTPOPUP.setMnemonic(KeyEvent.VK_A);

		CUTEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
		COPYEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
		PASTEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK));

		
		NEWFILE.addActionListener(this);
		OPENFILE.addActionListener(this);
		SAVEFILE.addActionListener(this);
		SAVEASFILE.addActionListener(this);
		EXITFILE.addActionListener(this);
	
		text.getDocument().addUndoableEditListener(new UndoListener());
		CUTEDIT.addActionListener(this);
		COPYEDIT.addActionListener(this);
		PASTEDIT.addActionListener(this);
		DELETEDIT.addActionListener(this);
		SELECTEDIT.addActionListener(this);
		TIMEDIT.addActionListener(this);
		WORDEDIT.addActionListener(this);
		FONTEDIT.addActionListener(this);
		
		
		FINDSEARCH.addActionListener(this);
		FINDNEXTSEARCH.addActionListener(this);
		
		
		ABOUTHELP.addActionListener(this);
		
		
		CUTPOPUP.addActionListener(this);
		COPYPOPUP.addActionListener(this);
		PASTEPOPUP.addActionListener(this);
		DELETEPOPUP.addActionListener(this);
		SELECTPOPUP.addActionListener(this);
		
		
		text.addMouseListener(new MouseAdapter()
		{
   		   public void mousePressed(MouseEvent e) 
   		   {
	   		   if (e.isPopupTrigger()) 
	   		   {
            			POPUP.show(e.getComponent(),e.getX(), e.getY());
           	   	   }
    	    	
    		   }
		    public void mouseReleased(MouseEvent e) 
		    {
				if (e.isPopupTrigger()) 
				{
            				POPUP.show(e.getComponent(),e.getX(), e.getY());
        			}
    		    }
		});
			

		
	/*	addWindowListener( new WindowAdapter() 
		{ public void windowClosing(WindowEvent evt) 
		  { 
			  int response = JOptionPane.showConfirmDialog(null, "Do you really want to quit?"); 
			  System.out.println("Inside Window Listener");
			  switch (response)
			  {
				case 0:
					{
					dispose();
					break; }
				
			  	case 2: 
			  	{
				  	//Myeditor x = new Myeditor();
				  	System.out.println("Inside 2");
				  	e=new Myeditor();
				  	e.setVisible(true);
			  		break;} 			    
		 	   } 
			  System.out.println("response is :="+response);
		  } 
		} ); */

		
		
		
		addWindowListener(new WindowAdapter()
		{ 
			public void windowClosing(WindowEvent e)
			{
				exitApln();
			}
		});	
	}

		public void actionPerformed(ActionEvent e)
	{
	
				if (e.getSource()==NEWFILE)
		{
			newfile();
		}

				if (e.getSource()==OPENFILE)
		{
			open();
		}
	
	
		if (e.getSource()==SAVEFILE)
		{
			save();
		}
		
	
		if (e.getSource()==SAVEASFILE)
		{
			opened=false;
			save();		
		}
		
		
		if (e.getSource()==EXITFILE)
		{
			exitApln();		
		}
		
		if ((e.getSource()==CUTEDIT)||(e.getSource()==CUTPOPUP))
		{
			text.cut();
		}
	
				if ((e.getSource()==COPYEDIT)||(e.getSource()==COPYPOPUP))
		{
			text.copy();
		}
	
			if ((e.getSource()==PASTEDIT)||(e.getSource()==PASTEPOPUP))
		{
			text.paste();
		}
		
	
		if ((e.getSource()==DELETEDIT)||(e.getSource()==DELETEPOPUP))
		{
			text.replaceSelection(null);
		}
		
	
		if ((e.getSource()==SELECTEDIT)||(e.getSource()==SELECTPOPUP))
		{
			text.selectAll();
		}
		
		 
		if (e.getSource()==TIMEDIT)
		{
			Date currDate;
			String dd;
			currDate = new java.util.Date();
			dd=currDate.toString();
			text.insert(dd,text.getCaretPosition());
		}
		
			if (e.getSource()==WORDEDIT)
		{
			if(WORDEDIT.isSelected())
			text.setLineWrap(true);
			else
			text.setLineWrap(false);
		}
		
		
		if (e.getSource()==FONTEDIT)
		{
			fontDialogBox fontS = new fontDialogBox();
		}
			
		
		if (e.getSource()==FINDSEARCH)
		{
			wholeText=text.getText();
			findString =JOptionPane.showInputDialog(null, "Find What", "Find",
JOptionPane.INFORMATION_MESSAGE);

			ind = wholeText.indexOf(findString,0);
			text.setCaretPosition(ind);
			text.setSelectionStart(ind);
			text.setSelectionEnd(ind+findString.length());
		}
		
			if (e.getSource()==FINDNEXTSEARCH)
		{
			wholeText= text.getText();
			findString = JOptionPane.showInputDialog(null, "Find What","Find Next",
JOptionPane.INFORMATION_MESSAGE);
			ind = wholeText.indexOf(findString, text.getCaretPosition());
			text.setCaretPosition(ind);
			text.setSelectionStart(ind);
			text.setSelectionEnd(ind+findString.length());
		}
		
				if (e.getSource()==ABOUTHELP)
		{
			JOptionPane.showMessageDialog(null, "This is a simple Text Editor application built using Java made by Aayush Kher",
			"About AykEditor",
			JOptionPane.INFORMATION_MESSAGE);	
	 	}
	}	

	
	public void newfile()
	{
		if(!text.getText().equals(""))
		{
			opened=false;
			int confirm = JOptionPane.showConfirmDialog(null,
			"Text in the Untitled file has changed. \n Do you want to save the changes?",
			"New File",
			JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
	
			if( confirm == JOptionPane.YES_OPTION )
			{
				save();
				text.setText(null);
			}
			else if( confirm == JOptionPane.NO_OPTION )
			{
				text.setText(null);
			}
		}
	}
	
	
	public void open()
	{
		text.setText(null);
		JFileChooser ch = new JFileChooser();
	    	ch.setCurrentDirectory(new File("."));
		ch.setFileFilter(new javax.swing.filechooser.FileFilter()
       		{
           		public boolean accept(File f)
        		{
              			return f.isDirectory()
               			|| f.getName().toLowerCase().endsWith(".java");
        		}
			public String getDescription()
        		{
     		           return "Java files";
           		}
         	});
		int result = ch.showOpenDialog(new JPanel());
		if(result == JFileChooser.APPROVE_OPTION) 
		{
			filename = String.valueOf(ch.getSelectedFile());
			setTitle(filename);
			opened=true;
			FileReader fr;
			BufferedReader br;
			
			try
			{
				fr=new FileReader (filename);
				br=new BufferedReader(fr);
				String s;
				while((s=br.readLine())!=null)
				{
					text.append(s);
					text.append("\n");
				}
				fr.close();
			}
			catch(FileNotFoundException ex)
                           {
				JOptionPane.showMessageDialog(this, "Requested file not found", "Error Dialog box", JOptionPane.ERROR_MESSAGE);}
                           
                        catch(Exception ex)
                           {System.out.println(ex);}
		}
	}

	
	public void save()
	{
		if(opened==true)
		{
			try
			{
				FileWriter f1 = new FileWriter(filename);
				f1.write(text.getText());
				f1.close();
				opened = true;
			}
			catch(FileNotFoundException ex)
                           {
				JOptionPane.showMessageDialog(this, "Requested file not found", "Error Dialog box", JOptionPane.ERROR_MESSAGE);}
			catch(IOException ioe){ioe.printStackTrace();}
		}
		else
		{
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("."));
			int result = fc.showSaveDialog(new JPanel());
					
			if(result == JFileChooser.APPROVE_OPTION) 
			{
				filename = String.valueOf(fc.getSelectedFile());
				setTitle(filename);
				try
				{
				FileWriter f1 = new FileWriter(filename);
				f1.write(text.getText());
				f1.close();
				opened = true;
				}
			catch(FileNotFoundException ex)
                           {
				JOptionPane.showMessageDialog(this, "Requested file not found", "Error Dialog box", JOptionPane.ERROR_MESSAGE);}

				catch(IOException ioe){ioe.printStackTrace();}
			}
			
		}
	}

	
	public void exitApln()
	{
		if(!text.getText().equals(""))
		{
			int confirm = JOptionPane.showConfirmDialog(null,
			"Text in the file has changed. \n Do you want to save the changes?",
			"Exit",
			JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
	
			if( confirm == JOptionPane.YES_OPTION )
			{
				save();
				dispose();
				System.exit(0);
			}
			else if( confirm == JOptionPane.CANCEL_OPTION )
			{
				e=new Myeditor();
				String s= text.getText();
				e.setVisible(true);
				e.text.setText(s);
			}
			else if( confirm == JOptionPane.NO_OPTION )
			{
				dispose();
				System.exit(0);
			}
		}
		else
		{
			System.exit(0);
		}
    	}
	
    	
	class UndoListener implements UndoableEditListener
	{
   		public void undoableEditHappened(UndoableEditEvent e) 
   		{
    		undo.addEdit(e.getEdit());
       		undoAction.update();
    		}	  
	}  
	
	
	class fontDialogBox extends JFrame implements ActionListener
	{
			
		String availableFontString[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		JList fontList = new JList(availableFontString);
		JLabel fontLabel = new JLabel("Font");
		JTextField valueFont=new JTextField("Arial");
		JScrollPane fontPane = new JScrollPane(fontList);
		
		
		String fontStyleString[] = {"Normal","Bold","Italic","Bold Italic"};
		JList styleList = new JList(fontStyleString);
		JLabel styleLabel = new JLabel("Style");
		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
		int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		JScrollPane stylePane = new JScrollPane(styleList,v,h);
		JTextField valueStyle=new JTextField("Normal");
	
		String fontSizeString[] = {"8","10","12","14","16","18","20","22","24","28"};
		JList sizeList = new JList(fontSizeString);
		JLabel sizeLabel = new JLabel("Font size");
		JScrollPane sizePane = new JScrollPane(sizeList);
		JTextField valueSize=new JTextField("12");
	
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
	
		JLabel sampleLabel = new JLabel("Sample:");
		JTextField sample = new JTextField("   AaBbCc");
	
		Font selectedFont;
		
			public fontDialogBox()
		{
			setSize(500,300);
			setTitle("Font");
			setVisible(true);
			sample.setEditable(false);
		
			getContentPane().setLayout(null);
		
			fontLabel.setBounds(10,10,170,20);
			valueFont.setBounds(10,35,170,20);
			fontPane.setBounds(10,60,170,150);
		
			styleLabel.setBounds(200,10,100,20);
			valueStyle.setBounds(200,35,100,20);
			stylePane.setBounds(200,60,100,150);
		
			sizeLabel.setBounds(320,10,50,20);
			valueSize.setBounds(320,35,50,20);
			sizePane.setBounds(320,60,50,150);
		
			okButton.setBounds(400,35,80,20);
			cancelButton.setBounds(400,60,80,20);
		
			sampleLabel.setBounds(150,235,50,30);
			sample.setBounds(200,235,100,30);
		
			getContentPane().add(fontLabel);
			getContentPane().add(fontPane);
			getContentPane().add(valueFont);
		
			getContentPane().add(styleLabel);
			getContentPane().add(stylePane);
			getContentPane().add(valueStyle);
		
			getContentPane().add(sizeLabel);
			getContentPane().add(sizePane);
			getContentPane().add(valueSize);
	
			getContentPane().add(okButton);
			getContentPane().add(cancelButton);
			getContentPane().add(sampleLabel);
			getContentPane().add(sample);
		
			okButton.addActionListener(this);
			cancelButton.addActionListener(this);
		
			fontList.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent event) 
				{
      					if (!event.getValueIsAdjusting()) 
      					{
	    				valueFont.setText(fontList.getSelectedValue().toString());
	    				selectedFont = new Font(valueFont.getText(),styleList.getSelectedIndex(),Integer.parseInt(valueSize.getText()));
	    				sample.setFont(selectedFont);
    					}
    				}
    			});
    	
    			styleList.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent event) 
				{
      				if (!event.getValueIsAdjusting()) 
      					{
	      				valueStyle.setText(styleList.getSelectedValue().toString());
	      				selectedFont = new Font(valueFont.getText(),styleList.getSelectedIndex(),Integer.parseInt(valueSize.getText()));
	    				sample.setFont(selectedFont);
      					}
      				}
    			});
    	
    			sizeList.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent event) 
				{
      					if (!event.getValueIsAdjusting()) 
      					{
      						valueSize.setText(sizeList.getSelectedValue().toString());
      						selectedFont = new Font(valueFont.getText(),styleList.getSelectedIndex(),Integer.parseInt(valueSize.getText()));
	    					sample.setFont(selectedFont);
      					}
 				}   	
      			});
		}
	
		public void actionPerformed(ActionEvent ae)
		{
			if(ae.getSource()==okButton)
			{
				selectedFont = new Font(valueFont.getText(),styleList.getSelectedIndex(),Integer.parseInt(valueSize.getText()));
	  		  	text.setFont(selectedFont);
				setVisible(false);
			}
			if(ae.getSource()==cancelButton)		
			{
				setVisible(false);
			}
		}
	}	

	
	public static void main(String args[])
	{
		e= new Myeditor();
	}
}


public class Project_17 extends JPanel {
 
  Image image;
 
  public Project_17() {
    image = Toolkit.getDefaultToolkit().createImage("C:/Users/Aayu/Desktop/TextEditor/spin.gif");
  }
 
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (image != null) {
      g.drawImage(image, 50, 50, this);
    }
  }
 
public static void main(String args[]) {

    final JFrame frame = new JFrame("Please Wait...");
    frame.setSize(200, 200);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Project_17 background = new Project_17();
    frame.add(background);


    Timer timer = new Timer(4000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            frame.setVisible(false);
            frame.dispose();
           //I want to place my code here so then this class will close, and then the other class will open
      
        new Myeditor();
      

        }
         
    });
        timer.setRepeats(false);
    timer.start();

   
}

}


