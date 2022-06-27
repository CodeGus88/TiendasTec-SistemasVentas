/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tools;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Toast extends Thread{
    
    private final JFrame frame;
    private final JLabel textMessage;
    private final int time;
    private final int LENGTH_LONG = 5;
    private final int LENGTH_SHORT = 3;
    
    public Toast(String message, int sconds){
        frame = new JFrame();
        frame.setSize(1000, 50);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        frame.setUndecorated(true);
        frame.setAlwaysOnTop( true ); // siempre visible
        this.textMessage = new JLabel(
        "<html><span style='font-size:18px'>"+message+"</span></html>"     
        );
        textMessage.setSize(1000, 50);
        textMessage.setHorizontalAlignment(SwingConstants.CENTER);
        textMessage.setForeground(Color.WHITE);
        textMessage.setOpaque(false);
        this.time = sconds*1000;
        frame.add(textMessage );
       
//        Process process = new Process();
//        process.run();
        run();
                
    }

    public void run(){
        frame.setVisible(true);
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Logger.getLogger(Toast.class.getName()).log(Level.SEVERE, null, ex);
        }
        frame.dispose();
    }
}