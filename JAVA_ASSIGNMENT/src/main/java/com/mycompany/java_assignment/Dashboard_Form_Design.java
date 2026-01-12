/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.java_assignment;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*; // Ensures Dimension, BorderLayout,and related AWT classes
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*; 
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

 

/**
 *
 * @author user
 */

public class Dashboard_Form_Design {
           private JFrame frame;
           private JPanel titleBar;
           private JLabel titleLabel;
           private JLabel minimizeLabel;
           private JPanel dashboardPanel;
           private JLabel closeLabel;
           
           // variables for form dragging
           private boolean isDragging = false;
           private Point mouseOffset;
           
           public Dashboard_Form_Design()
           {
               frame = new JFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setSize(1000,600); //Adjusts the width and height of frame
               frame.setLocationRelativeTo(null);
               frame.setUndecorated(true);
               frame.setBackground(new Color(255, 159,26));
               
               
               /*Container contentPane = frame.getContentPane();
                 contentPane.setBackground(new Color(255, 159,26));
               */
               
               
               //Set a custom rounded border to the form
               frame.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                       new RoundedBorder(5,new Color(255,255,255)),
                       new EmptyBorder(0,0,0,0)
               
               ));
                
               //form title bar (top of the form)
               titleBar = new JPanel();
               titleBar.setLayout(null);
               titleBar.setBackground(new Color(75,75,75));
               titleBar.setPreferredSize(new Dimension(800, 30));
               frame.add(titleBar,BorderLayout.NORTH);
               
               
               //form title bar(in the tilebar)
               titleLabel = new JLabel("Dashboard");
               titleLabel.setForeground(Color.white);
               titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
               titleLabel.setBounds(10,0,200,30);
               titleBar.add(titleLabel);
               
               // close the form label
               closeLabel = new JLabel("X");
               closeLabel.setForeground(Color.white);
               closeLabel.setHorizontalAlignment(SwingConstants.CENTER);
               closeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
               closeLabel.setBounds(frame.getWidth() - 40,0,30,30);
               titleBar.add(closeLabel);
               
   
               // minimze the form label
               minimizeLabel = new JLabel("-");
               minimizeLabel.setForeground(Color.white);
               minimizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
               minimizeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
               minimizeLabel.setBounds(frame.getWidth() - 80,0,30,30);
               titleBar.add(closeLabel);
               
               closeLabel.addMouseListener(new MouseAdapter() {
                   
                   @Override
                   public void mouseClicked(MouseEvent e)
                   {
                       System.exit(0);
                   }
                   @Override
                   public void mouseEntered(MouseEvent e)
                   {
                       minimizeLabel.setForeground(Color.yellow);
                   }
                   @Override
                   public void mouseExited(MouseEvent e)
                   {
                       minimizeLabel.setForeground(Color.white);
                   }


               });
               
               
               dashboardPanel = new JPanel();
               dashboardPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,20));
               dashboardPanel.setBackground(new Color(30,30,30));
               frame.add(dashboardPanel,BorderLayout.CENTER);
               
              //Add data panels 
              addDataPanel(" Total Revenue","RM700");
              addDataPanel("Vendor Revenue","RM900");
              addDataPanel("Monthly Revenue","RM1,000");
              addDataPanel("Order Value","RM2,000");
                
                 //Display the Chart Panel
                 JPanel chartPanel = new JPanel(){
                 
                      @Override
                      protected void paintComponent(Graphics g)
                      {
                          super.paintComponent(g);
                          //draw the chart 
                          drawChart(g,getHeight());
                      }
                 
                 
                 };
                 
                 chartPanel.setLayout(new BorderLayout());
                 chartPanel.setPreferredSize(new Dimension(740,300));
                 chartPanel.setBackground(Color.DARK_GRAY);
                 chartPanel.setBorder(new LineBorder(new Color(255,204,0),1));
                 
                 //Create a title for the chart panel
                 JLabel chartTitleLabel = new JLabel("Delivery Runner Performance");
                 chartTitleLabel.setFont(new Font("Arial", Font.BOLD,20));
                 chartTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                 chartTitleLabel.setOpaque(true);
                 chartTitleLabel.setBackground(new Color(255,87,34));
                 chartTitleLabel.setForeground(Color.white);
                 chartPanel.add(chartTitleLabel,BorderLayout.NORTH);
                 
                 dashboardPanel.add(chartPanel);
                 
                 //form dragging 
                 //Mouse listener for window dragging
                 titleBar.addMouseListener(new MouseAdapter(){
                     
                     @Override
                     public void mousePressed(MouseEvent e)
                     {
                         isDragging = true;
                         mouseOffset = e.getPoint();
                     }
                     
                      @Override
                     public void mouseReleased(MouseEvent e)
                     {
                         isDragging = false;
                     }
                     
                 });
                 
                 //Mouse 
                 titleBar.addMouseMotionListener(new MouseAdapter(){
                  
                     @Override
                     public void mouseDragged(MouseEvent e)
                     {
                         if(isDragging)
                         {
                             //When the mouse is dragged, the event is triggered
                             //Get current location of the mouse on the screen
                             Point newLocation = e.getLocationOnScreen();
                             
                             //Calculate the new window location by adjusting for the initial mouse offset
                             newLocation.translate(-mouseOffset.x, -mouseOffset.y);
                             
                             //Set the new location of the main window to achieve dragging effect
                             frame.setLocation(newLocation);
                         }
                     }
                     
                  });
              
               frame.setVisible(true);
               
               
               
           }
    
            // Create a method to add data to the panel
           private void addDataPanel(String title, String value)
           {
               JPanel dataPanel = new JPanel(){
                   @Override
                   protected void paintComponent(Graphics g)
                   {
                       super.paintComponent(g);
                       //Drwa the Data Panel
                       drawDataPanel(g,title,value,getWidth(),getHeight());
                   }
               
               };
               
               dataPanel.setLayout(new GridLayout(2,1));
               dataPanel.setPreferredSize(new Dimension(190,100));
               dataPanel.setBackground(new Color(45,45,45));
               dataPanel.setBorder(new LineBorder(new Color(255,204,0),2));
               dashboardPanel.add(dataPanel);
           }
          
    
             // create a custom method to draw a date panel
           
           private void drawDataPanel(Graphics g,String title,String value,int width,int height)
           {
               
               Graphics2D g2d = (Graphics2D) g;
               g2d.setColor(new Color(45,45,45));
               g2d.fillRoundRect(0,0,width, height, 20, 20);
               
               // Background color for Data Panel Title
               g2d.setColor(new Color(30,30,30));
               g2d.fillRect(0,0, width,40);
               g2d.setColor(Color.white);
               g2d.setFont(new Font("Arial", Font.BOLD,20));
               g2d.drawString(title,20,30);
               
               //Value 
               g2d.setColor(Color.white);
               g2d.setFont(new Font("Arial", Font.PLAIN,16));
               g2d.drawString(value,20,75);
              
           }
           
           //Create a Custom method to draw a chart
            private void drawChart(Graphics g, int height)
            {
                Graphics2D g2d = (Graphics2D) g;
                
                //Chart appearance
                int barWidth = 60;
                int barSpacing = 55;
                int startX = 50;
                int startY = height - 80;
                
                int[]data = {4*50,3*50,5*50,4*50,2*50,5*50}; //RatingsScaled for visibility
                //Calculate maximum data value
                int maxDataValue = 0;
                for(int value : data)
                {
                    if(value > maxDataValue)
                    {
                        maxDataValue = value;
                    }
                }
                
                //Set Colors for bars and labels
                Color barColor = new Color(0,150,136);
                Color labelColor = Color.white;
                
                for(int i = 0; i < data.length; i++)
                {
                    int barHeight = (int)((double)data[i]/maxDataValue * (startY-60));
                    int x = startX + (barWidth + barSpacing) * i;
                    int y = startY - barHeight;
                    g2d.setColor(barColor);
                    g2d.fillRect(x, y, barWidth, barHeight);
                    
                    //Draw Data Labels
                    g2d.setColor(labelColor);
                    g2d.setFont(new Font("Arial",Font.BOLD,14));
                    g2d.drawString(String.valueOf(data[i]), x+10, y-10);
                    g2d.setFont(new Font("Arial",Font.PLAIN,12));
                    g2d.drawString("Delivery Runner Ratings" +(i+1), x+5, startY+20);
                }
            }
          
           
             public static void main(String[] args) {
        
                new Dashboard_Form_Design();
        }
             
    
}


                   //Create a custom border class for rounded corners
                     class RoundedBorder implements Border
                     {
                         
                         private int radius;
                         private Color color;
                         
                         public RoundedBorder(int radius,Color color)
                         {
                             this.radius = radius;
                             this.color = color;
                         }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.drawRoundRect(x,y,width-1,height-1,radius,radius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        
        return new Insets(radius,radius,radius,radius);
    }

    @Override
    public boolean isBorderOpaque() {
        
        return true;
    }
                         
     }

     