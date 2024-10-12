
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Controlador  implements ActionListener{
    private Vista v;
    private Modelo m;

    public Controlador(Vista v, Modelo m) {
        this.v = v;
        this.m = m;
        this.v.btnBuscar.addActionListener(this);
    }

    public void iniciar() {
        v.setTitle("Hipocoristicos");
        v.setLocationRelativeTo(null);
        v.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String resultado = "No son equivalentes";
        if(m.CompararPersonas(v.jTextPersona1.getText(), v.jTextPersona2.getText())){
            resultado = "Son equivalentes";
        }
        v.lblSonEquivalentes.setText(resultado);
    }

}
