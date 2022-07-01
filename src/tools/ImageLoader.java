/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tools;

//import java.awt.Image;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Gustavo
 */
public class ImageLoader {
    
    public static void setImage(JLabel lblImage, String path){
        try{
            ImageIcon imageIcon = new ImageIcon(path);
            Icon icon = new ImageIcon(
                    imageIcon.getImage().getScaledInstance(
                            lblImage.getWidth(),
                            lblImage.getHeight(),
                            Image.SCALE_DEFAULT
                    )
            );
            lblImage.setIcon(icon);
            lblImage.repaint();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
