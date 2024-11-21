
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author damia
 */
public class Controlador implements ActionListener{
    
    private Vista v;
    private Modelo m;
    
    public Controlador(Vista v, Modelo m){
        this.v = v;
        this.m = m;
        this.v.btTraducir.addActionListener(this);
        
    }
    
    public void iniciar(){
        v.setTitle("Traductor");
        v.setLocationRelativeTo(null);
        v.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        v.txtTraduccion.setText(m.traducir(v.txtPl0.getText()));
    }
}
