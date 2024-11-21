

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Controlador implements ActionListener{
    
    private Vista v;
    private Modelo m;
    
    public Controlador(Vista v, Modelo m){
        this.v = v;
        this.m = m;
        this.v.btnRecorrer.addActionListener(this);
        
    }
    
    public void iniciar(){
        v.setTitle("Recorrido");
        v.setLocationRelativeTo(null);
        v.btnRecorrer.setBackground(Color.GRAY);
        v.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        v.txtCoordenadasPlano.setText(m.obtenerPuntosCercanosText(Integer.parseInt(v.posicionX.getText()), Integer.parseInt(v.posicionY.getText())));
    }
}
