/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tools;

/**
 *
 * @author Gustavo Abasto Argote
 */
public class ScreenUses {
    public static int getPinX(float p){
        ScreenDimensions screenDimensions = new ScreenDimensions();
        return screenDimensions.pInX(p);
    }
    
    public static int getPinY(float p) {
        ScreenDimensions screenDimensions = new ScreenDimensions();
        return screenDimensions.pInY(p);
    }
}
