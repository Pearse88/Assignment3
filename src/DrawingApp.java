
//I used java abstract window toolkit and  instead of the processing JApplet and libraries 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DrawingApp extends JFrame
{
	
	private Canvas 		canvasPanel;
	private ToolButtonPanel   	toolButtonPanel;
	private ColourButtonPanel	colourButtonPanel;
	
	// variable for layout for screen i.e borders and tool bars
	private Container 			mainContainer;
	private String fileName;
	
	
	JMenuBar mainBar;
	JMenu fileMenu, editMenu, setColourMenuItem, aboutMenu;
	JMenuItem newItem, openItem, closeItem, saveItem, saveAsItem, exitItem, undoItem, redoItem, 
	penColourItem, backGroundItem, authorItem;
	
	public DrawingApp()
	{
		//sets heading
		super("Drawing Application");
		//Sets file name to null changes when user saves image
		setFileName(null);
		
		//create a menu bar
		mainBar 		= new JMenuBar();
		setJMenuBar(mainBar);
		
		//Tool-bar Heading
		fileMenu  		= new JMenu("File");
		
		//Drop down menu for File
		newItem		= new JMenuItem("New");
		openItem 	= new JMenuItem("Open");
		closeItem 	= new JMenuItem("Close"); 
		saveItem 	= new JMenuItem("Save");
		saveAsItem 	= new JMenuItem("Save As");
		exitItem	= new JMenuItem("Exit");
		
		//add Action Listener to menu buttons
		newItem.addActionListener(new MenuButtonListener());
		openItem.addActionListener(new MenuButtonListener());
		saveItem.addActionListener(new MenuButtonListener());
		saveAsItem.addActionListener(new MenuButtonListener());
		closeItem.addActionListener(new MenuButtonListener());
		exitItem.addActionListener(new MenuButtonListener());
		
		fileMenu.add(newItem);
		fileMenu.add(openItem);
		fileMenu.add(closeItem);
		fileMenu.addSeparator();
		fileMenu.add(saveItem);
		fileMenu.add(saveAsItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
			
		//Tool-bar Heading 
		editMenu = new JMenu("Edit");
		
		//Drop down menu for Edit
		undoItem	   = new JMenuItem("Undo");
		redoItem 	   = new JMenuItem("Redo");
		
		setColourMenuItem   = new JMenu("Set Colour");
		penColourItem = new JMenuItem("Set Pen Colour");
		backGroundItem = new JMenuItem("Set BackGround");
		
		undoItem.addActionListener(new MenuButtonListener());
		redoItem.addActionListener(new MenuButtonListener());
		penColourItem.addActionListener(new MenuButtonListener());
		backGroundItem.addActionListener(new MenuButtonListener());
		
		setColourMenuItem.add(penColourItem);
		setColourMenuItem.add(backGroundItem);
		
		editMenu.add(undoItem);
		editMenu.add(redoItem);
		editMenu.addSeparator();
		editMenu.add(setColourMenuItem);
			
		aboutMenu	= new JMenu("About");
		aboutMenu.setMnemonic('A');
		
		authorItem = new JMenuItem("Author");
		authorItem.addActionListener(new MenuButtonListener());
		
		aboutMenu.add(authorItem);
				
		mainBar.add(fileMenu);
		mainBar.add(editMenu);
		mainBar.add(aboutMenu);

		canvasPanel 	  = new Canvas();
		toolButtonPanel   = new ToolButtonPanel(canvasPanel);
		colourButtonPanel  = new ColourButtonPanel(canvasPanel);
		
		//border layout
		mainContainer = getContentPane();
		mainContainer.add(toolButtonPanel,BorderLayout.NORTH);
		mainContainer.add(canvasPanel,BorderLayout.CENTER);
		mainContainer.add(colourButtonPanel,BorderLayout.SOUTH);
		
		setSize(800,600);
		setVisible(true);
		
		//Window listener to check for user changes to the window
		addWindowListener (
      		new WindowAdapter () 
      		{
      			//Close Window
      			public void windowClosing (WindowEvent e) 
      			{
      				System.exit(0);
      			}
      			//Window changed from from a minimized to normal 
      			public void windowDeiconified (WindowEvent e) 
      			{
      				canvasPanel.repaint();
      			}
      			
      			//Make window active
      			public void windowActivated (WindowEvent e) 
      			{	 
      				canvasPanel.repaint();
      			}
      		}
      	); // end of addWindowListener()
	}

	
	//Menu button listener to track user interactions wit buttons
	public class MenuButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(event.getSource() == newItem || event.getSource() == closeItem)
			{
				canvasPanel.clearCanvas();
				canvasPanel.setDrawMode(0);
				canvasPanel.setpenColour(Color.BLACK);
				canvasPanel.setBackGroundColour(Color.WHITE);
				canvasPanel.repaint();
			}
			if(event.getSource() == exitItem)
			{
				System.exit(0);
			}
			if(event.getSource() == penColourItem)
			{
				colourButtonPanel.setpenColour();
				canvasPanel.repaint();
			}
			if(event.getSource() == backGroundItem)
			{
				colourButtonPanel.setBackGroundColour();
				canvasPanel.repaint();
			}
			if(event.getSource() == authorItem)
			{
				JOptionPane.showMessageDialog(DrawingApp.this,"Author : Pearse McLaughlin \n Student No.: C14727421","Drawing Application",JOptionPane.INFORMATION_MESSAGE);
				canvasPanel.repaint();
			}
			if(event.getSource() == saveItem)
			{
				canvasPanel.SaveCanvasToFile();
			}
			if(event.getSource() == saveAsItem)
			{
				canvasPanel.SaveAsCanvasToFile();
			}
			if(event.getSource() == openItem)
			{
				canvasPanel.OpenCanvasFile();
			}
			if(event.getSource() == undoItem)
			{
				canvasPanel.undo();
			}
			if(event.getSource() == redoItem)
			{
				canvasPanel.redo();
			}
		}
	} // end of MenuListener for buttons
	
	
	// main
	public static void main(String args[])
	{
		DrawingApp application = new DrawingApp();
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public String getFileName() 
	{
		return fileName;
	}
	public void setFileName(String fileName) 
	{
		this.fileName = fileName;
	}
}

