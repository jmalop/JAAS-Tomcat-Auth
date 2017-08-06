import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 *
 * @author Ivan Benaiges Trenchs
 */
public class Usuari {
    // Atributs referents als fitxers on es troben les dades:
    private static final String _sFUsers = System.getProperty("catalina.home") + "/conf/users.txt"; //"/opt/apache-tomcat-7.0.32/conf/users.txt";
    private static final String _sFShadow = System.getProperty("catalina.home") + "/conf/shadow.txt"; //"/opt/apache-tomcat-7.0.32/conf/shadow.txt";
    
    // Atributs de la classe referents a les dades de l'usuari:
    private String _sUsuari;
    private String _sNom;
    private String _sActivitat;
	private String _sRols;
    private String _sShadowedPassword;
    private String _sSalt;
    private boolean _bBloquejada;
    private int _iIntents;
   
    // Constructor de la classe, rep l'identificador d'usuari i cerca les seves
    // dades ens els fitxers indicats:
    public Usuari(String sUsuari) throws Exception
    {
        // Guardem l'identificador de l'usuari i busquem la informació del
        // mateix als fitxers
        this._sUsuari = sUsuari;
        this.buscaDadesUsuari();
    }
    
    // Mètodes get públics pels atributs de la classe:
    public String getUsuari()
    {
        return this._sUsuari;
    }
    
    public String getNom()
    {
        return this._sNom;
    }
    
    public String getActivitat()
    {
        return this._sActivitat;
    }

	public String getRols()
    {
        return this._sRols;
    }
    
    public String getShadowedPassword()
    {
        return this._sShadowedPassword;
    }
    
    public String getSalt()
    {
        return this._sSalt;
    }
    
    public boolean getBloquejada()
    {
        return this._bBloquejada;
    }
    
    public int getIntents()
    {
        return this._iIntents;
    }
    
    // Funció que obre els fitxers per buscar-ne les dades:
    private void buscaDadesUsuari() throws Exception
    {
        try 
        {
            // Primer hem de mirar el primer fitxer per veure si realment l'usu-
            // ari existeix al sistema:
            FileInputStream fs = new FileInputStream(this._sFUsers);
            DataInputStream in = new DataInputStream(fs);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String linea;
            boolean bTrobat = false;
            
            // Anirem llegint fins a trobar l'usuari o el final del fitxer:
            while (((linea = br.readLine()) != null) && !(bTrobat))
            {
                String[] temp = linea.split(":");
                
                if (this._sUsuari.equals(temp[0]))
                {                  
                    // Hem trobat l'usuari, guardem les dades d'aquest fitxer:
                    this._sNom = temp[1];
                    this._sActivitat = temp[2];
					this._sRols = temp[3];
                    
                    this._iIntents = Integer.parseInt(temp[4]);
                    
                    if (temp[5].equals("1"))
                    {
                        this._bBloquejada = true;
                    }
                    else
                    {
                        this._bBloquejada = false;
                    }
                    
                    // Tenim una part de les dades, hem d'obrir l'altre fitxer
                    // per completar la "fitxa" amb les dades de l'usuari:
                    in.close();
                    fs = new FileInputStream(this._sFShadow);
                    in = new DataInputStream(fs);
                    br = new BufferedReader(new InputStreamReader(in));
                    
                    while (((linea = br.readLine()) != null) && !(bTrobat))
                    {
                        temp = linea.split(":");
                
                        if (this._sUsuari.equals(temp[0]))
                        {
                            // Guardem les dades restants:
                            this._sSalt = temp[1];
                            this._sShadowedPassword = temp[2];
                            
                            // Ja hem trobat totes les dades, ja podem sortir:
                            bTrobat = true;
                        }
                    }
                }
            }
            
            // Tanquem el fitxer que tinguem obert:
            in.close();
        }
        catch (Exception e)
        {
            System.err.println("Error al llegir: " + e.getMessage());
        }
    }
    
    // Funció que permet incrementar el nombre d'intents fallats per part de
    // l'usuari:
    public void incrementaIntents() throws FileNotFoundException, IOException
    {
        // Si ja té 3 intents fallits o està bloquejat, no fem res:
        if (!(this._bBloquejada || (this._iIntents >= 3)))
        {
            // Incrementem la variable i comprovem si s'ha de bloquejar:
            this._iIntents++;
            if (this._iIntents >= 3)
            {
                this._bBloquejada = true;
            }
            
            // Guardarem els nous valors al fitxer corresponent. Necessitarem 
            // obrir el fitxer original per llegir-ne els valors:
            File inFile = new File(this._sFUsers);
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
            BufferedReader br = new BufferedReader(new FileReader(this._sFUsers));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
            String linia = null;
            
            // Anirem llegint i guardant totes les línies del fitxer fins la
            // última, tenint en compte d'editar la corresponent a l'usuari:
            while ((linia = br.readLine()) != null) 
            {
                String[] temp = linia.split(":");
                
                // Es tracta de l'usuari a actualitzar:
                if (this._sUsuari.equals(temp[0]))
                {
                    // Generem la nova línia:
                    String sModificada = this._sUsuari + ":" + this._sNom + ":" + this._sActivitat + ":" + this._sRols + ":" + String.valueOf(this._iIntents) + ":";
                    
                    if (this._bBloquejada == false)
                    {
                        sModificada += "0";
                    }
                    else
                    {
                        sModificada += "1";
                    }
                    
                    // Afegim la línia modificada al fitxer:
                    pw.println(sModificada);
                    pw.flush();
                }
                else
                {
                    // En cas contrari copiem directament la línia:
                    pw.println(linia);
                    pw.flush();
                }
            }
            
            // Tanquem els buffers:
            pw.close();
            br.close();
            
            // Esborrem el fitxer original:
            if (!inFile.delete()) 
            {
                System.out.println("No es pot esborrar el fitxer");
                return;
            } 
      
            //Renombrem el fitxer perquè substitueixi l'original:
            if (!tempFile.renameTo(inFile))
            {
                System.out.println("No es pot renombrar el fitxer");
            }
        }
    }
    
    // Funció que permet ressetejar el nombre d'intents fallats per part de
    // l'usuari:
    public void ressetejaIntents() throws FileNotFoundException, IOException
    {
        // Si ja té 3 intents fallits o està bloquejat, no fem res:
        if (!(this._bBloquejada))
        {
            // Incrementem la variable i comprovem si s'ha de bloquejar:
            this._iIntents = 0;
            
            // Guardarem els nous valors al fitxer corresponent. Necessitarem 
            // obrir el fitxer original per llegir-ne els valors:
            File inFile = new File(this._sFUsers);
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
            BufferedReader br = new BufferedReader(new FileReader(this._sFUsers));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
            String linia = null;
            
            // Anirem llegint i guardant totes les línies del fitxer fins la
            // última, tenint en compte d'editar la corresponent a l'usuari:
            while ((linia = br.readLine()) != null) 
            {
                String[] temp = linia.split(":");
                
                // Es tracta de l'usuari a actualitzar:
                if (this._sUsuari.equals(temp[0]))
                {
                    // Generem la nova línia:
                    String sModificada = this._sUsuari + ":" + this._sNom + ":" + this._sActivitat + ":" + this._sRols + ":" + String.valueOf(this._iIntents) + ":";
                    
                    if (this._bBloquejada == false)
                    {
                        sModificada += "0";
                    }
                    else
                    {
                        sModificada += "1";
                    }
                    
                    // Afegim la línia modificada al fitxer:
                    pw.println(sModificada);
                    pw.flush();
                }
                else
                {
                    // En cas contrari copiem directament la línia:
                    pw.println(linia);
                    pw.flush();
                }
            }
            
            // Tanquem els buffers:
            pw.close();
            br.close();
            
            // Esborrem el fitxer original:
            if (!inFile.delete()) 
            {
                System.out.println("No es pot esborrar el fitxer");
                return;
            } 
      
            //Renombrem el fitxer perquè substitueixi l'original:
            if (!tempFile.renameTo(inFile))
            {
                System.out.println("No es pot renombrar el fitxer");
            }
        }
    }
}
