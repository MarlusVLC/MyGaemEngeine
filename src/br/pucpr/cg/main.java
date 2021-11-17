package br.pucpr.cg;

import br.pucpr.mage.Window;

public class main {
    public static void main(String[] args){
//        new Window(new ControllableCube()).show();
//        new Window(new RotatingSquare()).show();
//        new Window(new CustomPlane(11,10)).show();
//        new Window(new CustomHeightMap("heights/mountains.jpg",10f, 0.9f)).show();
        new Window(new NewCameraScene(), "Playground", 800, 600).show();
//        new Window(new ProfCameraScene(), "Playground", 800, 600).show();
    }
}
