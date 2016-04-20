
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import javax.swing.*;


import javax.imageio.*;

public class Canvas extends JPanel implements MouseListener,MouseMotionListener, Serializable
{
	//variables for shapes
	protected final static int LINE=1,SQUARE=2,CIRCLE=3,POLYGON=4,ROUNDED_RECT=5,FREE_HAND=6,
								SOLID_SQUARE=22, SOLID_CIRCLE=33, SOLID_POLYGON=44,
								SOLID_ROUNDED_RECT=55;
	
	//Vectors for shapes
	protected static Vector<Serializable> vLine,vSquare,vCircle,vPolygon,vRoundedRect,vFreeHand,
						 	vSolidSquare,vSolidCircle,vSolidPolygon,vSolidRoundedRect,vFile,
						 	xPolygon, yPolygon;						 	
	
	//stack variables for undo and redo
	private Stack<StepInfo> undoStack, redoStack;
	
	//pen Colour
	private Color penColour, backGroundColour; 
	
	//variables for pen location and draw mode
	private int x1,y1,x2,y2,linex1,linex2,liney1,liney2, drawMode=0;
	
	private boolean solidMode, polygonBuffer;
	
	private File fileName;
					    
	
	public Canvas()
	{
		//make new vectors
		vLine 			= new Vector<Serializable>();
		vSquare 		= new Vector<Serializable>();
		vCircle			= new Vector<Serializable>();
		vPolygon		= new Vector<Serializable>();
		vRoundedRect	= new Vector<Serializable>();
		vFreeHand		= new Vector<Serializable>();
		vSolidSquare	= new Vector<Serializable>();
		vSolidCircle	= new Vector<Serializable>();
		vSolidPolygon	= new Vector<Serializable>();
		vSolidRoundedRect = new Vector<Serializable>();
		vFile			= new Vector<Serializable>();
		xPolygon		= new Vector<Serializable>();
		yPolygon		= new Vector<Serializable>();
		
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		//set solidMode and polygon buffer to false
		solidMode 		= false;
		polygonBuffer 	= false; 
		
		//set pen colour and background colour
		penColour = Color.BLACK;
		backGroundColour = Color.WHITE;
		setBackground(backGroundColour);
		
		//stack
		undoStack = new Stack<StepInfo>();
		redoStack = new Stack<StepInfo>();
		
		repaint();		
	}
		
	
	public void mousePressed(MouseEvent event)
	{
		x1 = linex1 = linex2 = event.getX();
        y1 = liney1 = liney2 = event.getY();
	}

	
	public void mouseClicked(MouseEvent event)
	{
		
	}
	public void mouseMoved(MouseEvent event)
	{
		
	}

	public void mouseReleased(MouseEvent event)
	{
		if (drawMode == LINE)
        {	
           	vLine.add(new Coordinate(x1,y1,event.getX(),event.getY(),penColour));
           	undoStack.push(new StepInfo(LINE ,new Coordinate(x1,y1,event.getX(),event.getY(),penColour)));
        }
        if (drawMode == SQUARE) 
        {
            //create a solid square
        	if(solidMode)
           	{
           		if(x1 > event.getX() || y1 > event.getY())
				{
           			vSolidSquare.add(new Coordinate(event.getX(),event.getY(),x1,y1,penColour));
           			undoStack.push(new StepInfo(SOLID_SQUARE, new Coordinate(event.getX(),event.getY(),x1,y1,penColour)));
           		}
           		else
           		{
           			vSolidSquare.add(new Coordinate(x1,y1,event.getX(),event.getY(),penColour));
           			undoStack.push(new StepInfo(SOLID_SQUARE, new Coordinate(x1,y1,event.getX(),event.getY(),penColour)));
           		}
           	}
            
        	//if not solid push co ordinates onto stack
        	else
            {
           		if(x1 > event.getX() || y1 > event.getY())
           		{
           			vSquare.add(new Coordinate(event.getX(),event.getY(),x1,y1,penColour));
           			undoStack.push(new StepInfo(SQUARE, new Coordinate(event.getX(),event.getY(),x1,y1,penColour)));
           		}
           		else
           		{
           			vSquare.add(new Coordinate(x1,y1,event.getX(),event.getY(),penColour));
           			undoStack.push(new StepInfo(SQUARE, new Coordinate(x1,y1,event.getX(),event.getY(),penColour)));
           		}
           	}
        }
        
       //Circle
        if (drawMode == Canvas.CIRCLE) 
        {
          	if(solidMode)
          	{
          		if(x1 > event.getX() || y1 > event.getY())
          		{
          			vSolidCircle.add(new Coordinate(event.getX(),event.getY(),x1,y1,penColour));
          			undoStack.push(new StepInfo(SOLID_CIRCLE, new Coordinate(event.getX(),event.getY(),x1,y1,penColour)));
          		}
          		else
          		{
          			vSolidCircle.add(new Coordinate(x1,y1,event.getX(),event.getY(),penColour));
          			undoStack.push(new StepInfo(SOLID_CIRCLE, new Coordinate(x1,y1,event.getX(),event.getY(),penColour)));
          		}
           	}
           	else
           	{
           		if(x1 > event.getX() || y1 > event.getY())
           		{
           			vCircle.add(new Coordinate(event.getX(),event.getY(),x1,y1,penColour));
           			undoStack.push(new StepInfo(CIRCLE, new Coordinate(event.getX(),event.getY(),x1,y1,penColour)));
           		}
           		else	
           		{
           			vCircle.add(new Coordinate(x1,y1,event.getX(),event.getY(),penColour));
           			undoStack.push(new StepInfo(CIRCLE, new Coordinate(x1,y1,event.getX(),event.getY(),penColour)));
           		}
           	}
        }
        
        //Polygon
        if (drawMode == Canvas.POLYGON || drawMode == Canvas.SOLID_POLYGON) 
        {
        	xPolygon.add(new Integer(event.getX()));
        	yPolygon.add(new Integer(event.getY()));
        	polygonBuffer = true;
        	repaint();       	      
        }
        
        //Rounded edge rectangle
        if (drawMode == Canvas.ROUNDED_RECT) 
        {
          	if(solidMode)
          	{
          		if(x1 > event.getX() || y1 > event.getY())
          		{
          			vSolidRoundedRect.add(new Coordinate(event.getX(),event.getY(),x1,y1,penColour));
          			undoStack.push(new StepInfo(SOLID_ROUNDED_RECT, new Coordinate(event.getX(),event.getY(),x1,y1,penColour)));
          		}
          		else
          		{
           			vSolidRoundedRect.add(new Coordinate(x1,y1,event.getX(),event.getY(),penColour));
           			undoStack.push(new StepInfo(SOLID_ROUNDED_RECT, new Coordinate(x1,y1,event.getX(),event.getY(),penColour)));
           		}
           	}
           	else
           	{
           		if(x1 > event.getX() || y1 > event.getY())
           		{
           			vRoundedRect.add(new Coordinate(event.getX(),event.getY(),x1,y1,penColour));
           			undoStack.push(new StepInfo(ROUNDED_RECT, new Coordinate(event.getX(),event.getY(),x1,y1,penColour)));
           		}
           		else
           		{
           			vRoundedRect.add(new Coordinate(x1,y1,event.getX(),event.getY(),penColour));
           			undoStack.push(new StepInfo(ROUNDED_RECT, new Coordinate(x1,y1,event.getX(),event.getY(),penColour)));
           		}
           	}
        }           
        x1=linex1=x2=linex2=0;
        y1=liney1=y2=liney2=0;
	}

	public void mouseEntered(MouseEvent event)
	{
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public void mouseExited(MouseEvent event)
	{
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public void mouseDragged(MouseEvent event)
	{
        x2 = event.getX();
        y2 = event.getY();
                        
        if (drawMode == Canvas.FREE_HAND) 
        {
            linex1 = linex2;
            liney1 = liney2;           
            linex2 = x2;
            liney2 = y2;
               
            vFreeHand.add(new Coordinate(linex1,liney1,linex2,liney2,penColour));
         	undoStack.push(new StepInfo(FREE_HAND, new Coordinate(linex1,liney1,linex2,liney2,penColour)));
         }
         repaint();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
      	redrawVectorBuffer(g);

	  	g.setColor(penColour);
      
      	//check line coordinates
	  	if (drawMode == LINE) 
      	{
        	g.drawLine(x1,y1,x2,y2);
      	}
      	
	  	//check circle coordinates
	  	if (drawMode == CIRCLE) 
      	{
      	 	if(solidMode)
      	 	{
         		if(x1 > x2 || y1 > y2)
         		{
         			g.fillOval(x2,y2,x1-x2,y1-y2);
         		}	
         		else
         		{
         			g.fillOval(x1,y1,x2-x1,y2-y1);
         		}
      	 	}
         	else
         	{
         		if(x1 > x2 || y1 > y2)
         		{
         			g.drawOval (x2,y2,x1-x2,y1-y2);
         		}
         		else
         		{
         			g.drawOval (x1,y1,x2-x1,y2-y1);
         		}
         	}
      	}
      	
	  	//check rounded rectangle coordinates
	  	if (drawMode == ROUNDED_RECT) 
      	{
         	if(solidMode)
         	{
         		if(x1 > x2 || y1 > y2)
         		{
         			g.fillRoundRect(x2,y2,x1-x2,y1-y2,25,25);
         		}
         		else
         		{
         			g.fillRoundRect(x1,y1,x2-x1,y2-y1,25,25);
         		}
         		}
         	else
         	{
         		if(x1 > x2 || y1 > y2)
         		{
         			g.drawRoundRect(x2,y2,x1-x2,y1-y2,25,25);
         		}
         		else
         		{
         			g.drawRoundRect(x1,y1,x2-x1,y2-y1,25,25);
         		}
         		}
      	}
      	
	  	//check square coordinates
	  	if (drawMode == SQUARE) 
      	{
      	 	if(solidMode)
      	 	{
      	 		if(x1 > x2 || y1 > y2)
      	 		{
      	 			g.fillRect (x2,y2,x1-x2,y1-y2);
      	 		}
      	 		else
      	 		{
      	 			g.fillRect (x1,y1,x2-x1,y2-y1);
      	 		}
      	 	}
         	else
         	{
         		if(x1 > x2 || y1 > y2)
         		{
         			g.drawRect (x2,y2,x1-x2,y1-y2);
         		}
         		else
         		{
         			g.drawRect (x1,y1,x2-x1,y2-y1);
         		}
         		
         	}
      	}
      	
      	
	  	if (drawMode == POLYGON || drawMode == SOLID_POLYGON)
      	{
      		int xPos[] = new int[xPolygon.size()];
      	 	int yPos[] = new int[yPolygon.size()];
      	 
      	 	for(int count=0;count<xPos.length;count++)
      	 	{
      	 		xPos[count] = ((Integer)(xPolygon.elementAt(count))).intValue();
      	 		yPos[count] = ((Integer)(yPolygon.elementAt(count))).intValue();
      	 	}
      	 	g.drawPolyline(xPos,yPos,xPos.length);
      	 	polygonBuffer = true;
	  	}
      	if (drawMode == FREE_HAND) 
      	{
         	g.drawLine(linex1,liney1,linex2,liney2);
      	}
	}

	public void setDrawMode(int mode)
	{
		drawMode = mode;
	}
	public int getDrawMode()
	{	
		return drawMode;	
	}

	public void setSolidMode(Boolean inSolidMode)
	{
		solidMode = inSolidMode.booleanValue();
	}
	public Boolean getSolidMode()
	{
		return Boolean.valueOf(solidMode);
	}

	public void setpenColour(Color inputColor)
	{
		penColour = inputColor;
	}
	public Color getpenColour()
	{
		return penColour;
	}

	public void setBackGroundColour(Color inputColor)
	{
		backGroundColour = inputColor;
		this.setBackground(backGroundColour);
	}
	public Color getBackGroundColour()
	{
		return backGroundColour;
	}

	//undo last action
	public void undo()
	{
		StepInfo tempInfo;
		
		if(undoStack.isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Can't Undo","Painter", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			tempInfo = (StepInfo)undoStack.pop();

			//switch case for different shapes
			switch(tempInfo.getStepType())
			{
				case 1:	vLine.remove(vLine.size()-1);
					break;
				case 2:	vSquare.remove(vSquare.size()-1);
					break;
				case 3:	vCircle.remove(vCircle.size()-1);
					break;
				case 4:	vPolygon.remove(vPolygon.size()-1);
					break;	
				case 5:	vRoundedRect.remove(vRoundedRect.size()-1);
					break;
				case 6:	vFreeHand.remove(vFreeHand.size()-1);
					break;
				case 22:vSolidSquare.remove(vSolidSquare.size()-1);
					break;
				case 33:vSolidCircle.remove(vSolidCircle.size()-1);
					break;
				case 44:vSolidPolygon.remove(vSolidPolygon.size()-1);
					break;
				case 55:vSolidRoundedRect.remove(vSolidRoundedRect.size()-1);
					break;
			}
			redoStack.push(tempInfo);
		}
		repaint();
	}

	// redo last action
	public void redo()
	{
		StepInfo tempInfo;
		
		if(redoStack.isEmpty())
		{
			JOptionPane.showMessageDialog(null,"Can't Redo","Painter",JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			tempInfo = (StepInfo)redoStack.pop();
			
			switch(tempInfo.getStepType())
			{
				case 1:	vLine.add(tempInfo.getStepCoordinate());
					break;
				case 2:	vSquare.add(tempInfo.getStepCoordinate());
					break;
				case 3:	vCircle.add(tempInfo.getStepCoordinate());
					break;
				case 4:	vPolygon.add(tempInfo.getStepCoordinate());
					break;	
				case 5:	vRoundedRect.add(tempInfo.getStepCoordinate());
					break;
				case 6:	vFreeHand.add(tempInfo.getStepCoordinate());
					break;
				case 22:vSolidSquare.add(tempInfo.getStepCoordinate());
					break;
				case 33:vSolidCircle.add(tempInfo.getStepCoordinate());
					break;
				case 44:vSolidPolygon.add(tempInfo.getStepCoordinate());
					break;
				case 55:vSolidRoundedRect.add(tempInfo.getStepCoordinate());
					break;
			}
			undoStack.push(tempInfo);
		}
		repaint();
	}

	public void clearCanvas()
	{
		vFreeHand.removeAllElements();
		vLine.removeAllElements();
		vCircle.removeAllElements();
		vPolygon.removeAllElements();
		vRoundedRect.removeAllElements();
		vSolidCircle.removeAllElements();
		vSolidPolygon.removeAllElements();
		vSolidRoundedRect.removeAllElements();
		vSolidSquare.removeAllElements();
		vSquare.removeAllElements();
		undoStack.clear();
		redoStack.clear();
		repaint();
	}
	
	public void SaveCanvasToFile()
	{
		if(fileName != null)
		{
			vFile.removeAllElements();
			vFile.addElement(vFreeHand);
			vFile.addElement(vLine);
			vFile.addElement(vCircle);
			vFile.addElement(vPolygon);
			vFile.addElement(vRoundedRect);
			vFile.addElement(vSolidCircle);
			vFile.addElement(vSolidPolygon);
			vFile.addElement(vSolidRoundedRect);
			vFile.addElement(vSolidSquare);
			vFile.addElement(vSquare);
			vFile.addElement(new Color(backGroundColour.getRGB()));
			RenderedImage rendImage = myCreateImage();
			
			try
			{
				FileOutputStream fos = new FileOutputStream(fileName);
				@SuppressWarnings("resource")
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(vFile);
				JOptionPane.showMessageDialog(null,"File Saved","Painter",JOptionPane.INFORMATION_MESSAGE);
			}
			catch(Exception exp)
			{
				
			}
			
			try 
			{
        		File file = new File(fileName.toString() + ".jpg");        		
        		ImageIO.write(rendImage, "jpg", file);
    		}
			catch (IOException e) 
			{
				
			}
		}
		else
		{
			SaveAsCanvasToFile();
		}
		repaint();
	}
	
	//saveAs canvas to file
	public void SaveAsCanvasToFile()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);	
		int result = fileChooser.showSaveDialog(null);
	
		if(result == JFileChooser.CANCEL_OPTION) return;
		{
			fileName = fileChooser.getSelectedFile();

			if(fileName == null || fileName.getName().equals(""))
			{
				JOptionPane.showMessageDialog(null,"Invalid File Name","Painter",JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				vFile.removeAllElements();
				vFile.addElement(vFreeHand);
				vFile.addElement(vLine);
				vFile.addElement(vCircle);
				vFile.addElement(vPolygon);
				vFile.addElement(vRoundedRect);
				vFile.addElement(vSolidCircle);
				vFile.addElement(vSolidPolygon);
				vFile.addElement(vSolidRoundedRect);
				vFile.addElement(vSolidSquare);
				vFile.addElement(vSquare);	
				vFile.addElement(new Color(backGroundColour.getRGB()));
				
				RenderedImage rendImage = myCreateImage();
				
				try
				{
					FileOutputStream fos = new FileOutputStream(fileName);
					@SuppressWarnings("resource")
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(vFile);
					JOptionPane.showMessageDialog(null,"File Saved","Painter",JOptionPane.INFORMATION_MESSAGE);
				}
				catch(Exception exp)
				{
					
				}
				
				try {
	        		File file = new File(fileName.toString() + ".jpg");        		
	        		ImageIO.write(rendImage, "jpg", file);
	    		}
				catch (IOException e) 
				{
					
				}
			}
		}
	repaint();
	}

	@SuppressWarnings("unchecked")
	public void OpenCanvasFile()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
		int result = fileChooser.showOpenDialog(null);
		if(result == JFileChooser.CANCEL_OPTION) return;
			
		fileName = fileChooser.getSelectedFile();
		
		if(fileName != null)
		{
			try{
				FileInputStream fis = new FileInputStream(fileName);
				@SuppressWarnings("resource")
				ObjectInputStream ois = new ObjectInputStream(fis);
				vFile = (Vector<Serializable>) ois.readObject();
				
				this.clearCanvas();
				vFreeHand 		= (Vector<Serializable>)vFile.elementAt(0);
				vLine 			= (Vector<Serializable>)vFile.elementAt(1);
				vCircle			= (Vector<Serializable>)vFile.elementAt(2);
				vPolygon		= (Vector<Serializable>)vFile.elementAt(3);
				vRoundedRect	= (Vector<Serializable>)vFile.elementAt(4);
				vSolidCircle	= (Vector<Serializable>)vFile.elementAt(5);
				vSolidPolygon	= (Vector<Serializable>)vFile.elementAt(6);
				vSolidRoundedRect = (Vector<Serializable>)vFile.elementAt(7);
				vSolidSquare	= (Vector<Serializable>)vFile.elementAt(8);
				vSquare			= (Vector<Serializable>)vFile.elementAt(9);
				backGroundColour = (Color)vFile.elementAt(10);
				
				this.setBackground(backGroundColour);
			}
			catch(Exception exp){
				JOptionPane.showMessageDialog(null,"Can't Open File","Painter",JOptionPane.INFORMATION_MESSAGE);
			}	
		}
		else{
			fileName = null;
		}
		repaint();
	}

	public boolean isExistPolygonBuffer()
	{
		return polygonBuffer;
	} 
	
	public void flushPolygonBuffer()
	{
		if(!solidMode)
		{
			vPolygon.add(new Coordinate(xPolygon, yPolygon, penColour));
			undoStack.push(new StepInfo(POLYGON,new Coordinate(xPolygon, yPolygon, penColour)));
		}
		else
		{
			vSolidPolygon.add(new Coordinate(xPolygon, yPolygon, penColour));
			undoStack.push(new StepInfo(SOLID_POLYGON,new Coordinate(xPolygon, yPolygon, penColour)));
		}
		
		xPolygon.removeAllElements();
		yPolygon.removeAllElements();
			
		polygonBuffer = false;
		repaint();
	}

	private class Coordinate implements Serializable
	{
		private int x1,y1,x2,y2;
		private Color foreColor;
		private Vector<Serializable> xPoly, yPoly;
		
		public Coordinate (int inx1,int iny1,int inx2, int iny2, Color color) 
		{
        	x1 = inx1;
         	y1 = iny1;
         	x2 = inx2;
         	y2 = iny2;
         	foreColor = color;
      	}
      	@SuppressWarnings("unchecked")
		public Coordinate(Vector<Serializable> inXPolygon, Vector<Serializable> inYPolygon, Color color)
      	{
      		xPoly = (Vector<Serializable>)inXPolygon.clone();
      		yPoly = (Vector<Serializable>)inYPolygon.clone();
      		foreColor = color;
      	}
      	public Color colour()
      	{
        	return foreColor;
      	}
      	public int getX1 () 
      	{
        	return x1;
      	}
      	public int getX2 () 
      	{
        	return x2;
      	}
      	public int getY1 () 
      	{
        	return y1;
      	}
      	public int getY2 () 
      	{
        	return y2;
      	}
      	public Vector<Serializable> getXPolygon()
      	{
      		return xPoly;
      	}
      	public Vector<Serializable> getYPolygon()
      	{
      		return yPoly;
      	}
	}		
	
	private class StepInfo implements Serializable
	{
		private int stepType;
		private Coordinate stepCoordinate;
		
		public StepInfo(int inStepType, Coordinate inStepCoordinate)
		{
			stepType = inStepType;
			stepCoordinate = inStepCoordinate;
		}
		public int getStepType()
		{
			return stepType;
		}
		public Coordinate getStepCoordinate()
		{
			return stepCoordinate;
		}
	}
	
	private RenderedImage myCreateImage() 
	{
        BufferedImage bufferedImage = new BufferedImage(600,390, BufferedImage.TYPE_INT_RGB);

        Graphics g = bufferedImage.createGraphics();
    	redrawVectorBuffer(g);

      	g.dispose();
      	return bufferedImage;
    }
	
    private void redrawVectorBuffer(Graphics g)
    {
    	for (int i=0;i<vFreeHand.size();i++)
    	{
        	g.setColor(((Coordinate)vFreeHand.elementAt(i)).colour());
         	g.drawLine(((Coordinate)vFreeHand.elementAt(i)).getX1(),((Coordinate)vFreeHand.elementAt(i)).getY1(),
         	((Coordinate)vFreeHand.elementAt(i)).getX2(),((Coordinate)vFreeHand.elementAt(i)).getY2());
      	}
    	
      	for (int i=0;i<vLine.size();i++)
      	{
         	g.setColor(((Coordinate)vLine.elementAt(i)).colour());
         	g.drawLine(((Coordinate)vLine.elementAt(i)).getX1(),((Coordinate)vLine.elementAt(i)).getY1(),
         	((Coordinate)vLine.elementAt(i)).getX2(),((Coordinate)vLine.elementAt(i)).getY2());
      	}
      	
	  	for (int i=0;i<vCircle.size();i++)
	  	{	
         	g.setColor(((Coordinate)vCircle.elementAt(i)).colour());
         	g.drawOval(((Coordinate)vCircle.elementAt(i)).getX1(),((Coordinate)vCircle.elementAt(i)).getY1(),
         	((Coordinate)vCircle.elementAt(i)).getX2()-((Coordinate)vCircle.elementAt(i)).getX1(),
         	((Coordinate)vCircle.elementAt(i)).getY2()-((Coordinate)vCircle.elementAt(i)).getY1());
      	}
	  	
      	for (int i=0;i<vRoundedRect.size();i++)
      	{
         	g.setColor(((Coordinate)vRoundedRect.elementAt(i)).colour());
         	g.drawRoundRect(((Coordinate)vRoundedRect.elementAt(i)).getX1(),
         	((Coordinate)vRoundedRect.elementAt(i)).getY1(),((Coordinate)vRoundedRect.elementAt(i)).getX2()-((Coordinate)vRoundedRect.elementAt(i)).getX1(),
         	((Coordinate)vRoundedRect.elementAt(i)).getY2()-((Coordinate)vRoundedRect.elementAt(i)).getY1(),25,25);
      	}
      	for (int i=0;i<vSolidCircle.size();i++){
         	g.setColor(((Coordinate)vSolidCircle.elementAt(i)).colour());
         	g.fillOval(((Coordinate)vSolidCircle.elementAt(i)).getX1(),((Coordinate)vSolidCircle.elementAt(i)).getY1(),((Coordinate)vSolidCircle.elementAt(i)).getX2()-((Coordinate)vSolidCircle.elementAt(i)).getX1(),((Coordinate)vSolidCircle.elementAt(i)).getY2()-((Coordinate)vSolidCircle.elementAt(i)).getY1());
      	}
      	for (int i=0;i<vSolidRoundedRect.size();i++){
         	g.setColor(((Coordinate)vSolidRoundedRect.elementAt(i)).colour());
       	 	g.fillRoundRect(((Coordinate)vSolidRoundedRect.elementAt(i)).getX1(),((Coordinate)vSolidRoundedRect.elementAt(i)).getY1(),((Coordinate)vSolidRoundedRect.elementAt(i)).getX2()-((Coordinate)vSolidRoundedRect.elementAt(i)).getX1(),((Coordinate)vSolidRoundedRect.elementAt(i)).getY2()-((Coordinate)vSolidRoundedRect.elementAt(i)).getY1(),25,25);
      	}
      	for (int i=0;i<vSquare.size();i++){
         	g.setColor(((Coordinate)vSquare.elementAt(i)).colour());
         	g.drawRect(((Coordinate)vSquare.elementAt(i)).getX1(),((Coordinate)vSquare.elementAt(i)).getY1(),((Coordinate)vSquare.elementAt(i)).getX2()-((Coordinate)vSquare.elementAt(i)).getX1(),((Coordinate)vSquare.elementAt(i)).getY2()-((Coordinate)vSquare.elementAt(i)).getY1());
      	}
      	for (int i=0;i<vSolidSquare.size();i++){
         	g.setColor(((Coordinate)vSolidSquare.elementAt(i)).colour());
         	g.fillRect(((Coordinate)vSolidSquare.elementAt(i)).getX1(),((Coordinate)vSolidSquare.elementAt(i)).getY1(),((Coordinate)vSolidSquare.elementAt(i)).getX2()-((Coordinate)vSolidSquare.elementAt(i)).getX1(),((Coordinate)vSolidSquare.elementAt(i)).getY2()-((Coordinate)vSolidSquare.elementAt(i)).getY1());
      	}
      	for(int i=0;i<vPolygon.size();i++){
      	 	int xPos[] = new int[((Coordinate)vPolygon.elementAt(i)).getXPolygon().size()];
      	 	int yPos[] = new int[((Coordinate)vPolygon.elementAt(i)).getYPolygon().size()];
      	 
      	 	for(int count=0;count<xPos.length;count++)
      	 	{
      	 		xPos[count] = ((Integer)((Coordinate)vPolygon.elementAt(i)).getXPolygon().elementAt(count)).intValue();
      	 		yPos[count] = ((Integer)((Coordinate)vPolygon.elementAt(i)).getYPolygon().elementAt(count)).intValue();
      	 	}     	 
      	 	g.setColor(((Coordinate)vPolygon.elementAt(i)).colour());
      	 	g.drawPolygon(xPos,yPos,xPos.length);
	  	}
	  	for(int i=0;i<vSolidPolygon.size();i++){
      	 	int xPos[] = new int[((Coordinate)vSolidPolygon.elementAt(i)).getXPolygon().size()];
      	 	int yPos[] = new int[((Coordinate)vSolidPolygon.elementAt(i)).getYPolygon().size()];
      	 
      	 	for(int count=0;count<xPos.length;count++)
      	 	{
      	 		xPos[count] = ((Integer)((Coordinate)vSolidPolygon.elementAt(i)).getXPolygon().elementAt(count)).intValue();
      	 		yPos[count] = ((Integer)((Coordinate)vSolidPolygon.elementAt(i)).getYPolygon().elementAt(count)).intValue();
      	 	}
      	 	g.setColor(((Coordinate)vSolidPolygon.elementAt(i)).colour());
      	 	g.fillPolygon(xPos,yPos,xPos.length);
      	}	
    }
    
}