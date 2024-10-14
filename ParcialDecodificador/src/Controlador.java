
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controlador implements ActionListener{
    
    private Vista v;
    private Modelo m;
    
    public Controlador(Vista v, Modelo m){
        this.v = v;
        this.m = m;
        this.v.btnCodificar.addActionListener(this);
        
    }
    
    public void iniciar(){
        v.setTitle("Codificador a BinarioSSSS");
        v.setLocationRelativeTo(null);
        v.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        v.txtBinario.setText("");
        v.txtBinario.setText(m.traducirTexto(v.txtHumano.getText().toUpperCase()));
    }
}