import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Ivan Benaiges Trenchs
 */
public class Autenticador {   
    // Definim els atributs de la classe, dos d'ells s'especificaran al generar
    // l'objecte, mentres que l'últim es generarà al construir-lo:
    private String _sUsuari;
    private char[] _cContrassenya;
	private String _sRols;
    private Usuari _cDadesUsuari;
    
    // L'últim atribut contindrà les dades de l'usuari que hem llegit als 
    // fitxers del sistema:
    
    // Constructor de la classe, necessita rebre l'usuari i la contrassenya:
    public Autenticador(String sUsuari, char[] cContrassenya) throws Exception
    {
        this._sUsuari = sUsuari;
        this._cContrassenya = cContrassenya;
        this._cDadesUsuari = new Usuari(this._sUsuari);
		this._sRols = this._cDadesUsuari.getRols();
    }

	// Funció que retorna el llistat de rols de l'usuari:
	public String getRols()
	{
		return this._sRols;
	}
    
    // Mètode per autenticar les credencials de l'usuari, retorna true si són
    // correctes i false en cas contrari:
    public boolean comprobarCredencials() throws Exception
    {
        // És important tenir en compte si existeix el compte d'usuari i si 
        // realment no està bloquejat:
        if ((this._cDadesUsuari.getShadowedPassword() != null) && this._cDadesUsuari.getBloquejada() == false)
        {
            // Primer hem d'utilitzar les dades que hem carregat de l'usuari per
            // calcularne el hash de la contrassenya i comparar-la amb l'obtinguda
            // del fitxer per aquest usuari:
            String sShadowedPassword = this.getShadowedPassword();
            
            // Comparem si realment els valors coincideixen:
            if (sShadowedPassword.equals(this._cDadesUsuari.getShadowedPassword()))
            {
                // Si encara no s'ha bloquejat el compte, hauriem de poder 
                // reiniciar el comptador d'intents erronis:
                this._cDadesUsuari.ressetejaIntents();
                return true;
            }
            else
            {
                // En cas d'error entre les contrassenyes marquem un intent 
                // invàlid per l'usuari en concret: 
                this._cDadesUsuari.incrementaIntents();
                return false;
            }
        }
        
        // Les credencials no són correctes, l'usuari no existeix:
        return false;
    }
    
    // Mètode per calcular el hash de la contrassenya (obtenir un shadowed 
    // password):
    private String getShadowedPassword() throws UnsupportedEncodingException
    {
        // Realitzem un primer càlcul sumant els caràcters que formen els tres
        // camps de l'usuari (id, contrassenya i salt), per obtenir un primer
        // valor a partir del qual continuar:
        char[] cUsuari = this._sUsuari.toCharArray();
        char[] cSalt = this._cDadesUsuari.getSalt().toCharArray();
        char[] cHash = {' ',' ',' ',' ',' ',' ',' ',' '};
        StringBuilder sb = new StringBuilder();
                
        for (int i=0; i<this._cContrassenya.length; i++)
        {
            cHash[i] = (char)((((this._cContrassenya[i] + cSalt[i]) + 256) * 33 ) % 256 );
            
            if (i < cUsuari.length) 
            {
                cHash[i] = (char)((((cHash[i] + cUsuari[i]) + 256) * 33 ) % 256 );
            }
                        
            sb.append(Integer.toHexString(cHash[i]));
        }

        String sHash = sb.toString(); 

	// Calculem el valor del shadowed password utilitzant l'algoritme MD5:        
        byte[] digest = null;
        byte[] buffer = sHash.getBytes("UTF-8");
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(buffer);
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error creando Digest: " + ex.getMessage());
        }
        
        sHash="";
        for(byte aux : digest) 
        {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) 
            {
                sHash += "0";
            }
            sHash += Integer.toHexString(b);
        }
        
        // Retornem el valor final després d'utilitzar MD5:
        return sHash;
    }
}
