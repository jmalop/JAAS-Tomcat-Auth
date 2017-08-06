import java.io.IOException;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/**
 *
 * @author Ivan Benaiges Trenchs
 */
public class IDLoginModule implements LoginModule 
{
    // Atributs específics per mantenir l'estat del login:
    private Subject _oSubject;
    private CallbackHandler _oCallbackHandler;
    private Map _oSharedState;
    private Map _oOptions;
    
    // Atributs per l'estat de l'autenticació:
    private boolean _bSucceded = false;
    private boolean _bCommitSucceded = false;
    
    // Atribut que contindrà les credencials de l'usuari:
    private String _sUsuari;
    private char[] _cContrassenya;
	private String _sRols;
    
    // Principal d'usuari:
    private IDPrincipalUsuari _oPrincipalUsuari = null;
    
    // El constructor serà el de per defecte de la classe LoginModule:
    public IDLoginModule()
    {
        super();
    }
    
    @Override
    public boolean abort() throws LoginException 
    {
        // Si no hem aconseguit autenticar-nos, no cal fer res:
        if (!(this._bSucceded))
        {
            return false;
        }
        else if (this._bSucceded && this._bCommitSucceded)
        {
            // Reiniciem tots els objectes amb dades:
            this._bSucceded = false;
            this._sUsuari = null;
            this._cContrassenya = null;
            this._oPrincipalUsuari = null;
        }
        else
        {
            // Seguim un logout sense preocupar-nos massa:
            this.logout();
        }
        
        return true;
    }
    
    @Override
    public boolean commit() throws LoginException 
    {
        // Si no hem aconseguit autenticar l'usuari, no seguirem:
        if (!(this._bSucceded))
        {
            this._bCommitSucceded = false;
            return false;
        }
        else
        {
            // Hem d'afegir el Principal de l'usuari al nostre subject si no
            // existeix encara dins el mateix:
            this._oPrincipalUsuari = new IDPrincipalUsuari(this._sUsuari);
            if (!(this._oSubject.getPrincipals().contains(this._oPrincipalUsuari)))
            {
                this._oSubject.getPrincipals().add(this._oPrincipalUsuari);
            }
            
            // Afegim els diferents Principal rols de l'usuari al subject:
			String[] rols = this._sRols.split("[.]");

System.err.println("Rols IDLoginModule.class: " + this._sRols);
System.err.println("Rols length: " + this._sRols.split("[.]"));
System.err.println("Rols length: " + rols.length);

			for (int i=0; i<rols.length; i++)
			{
				IDPrincipalRol oPrincipalRol = new IDPrincipalRol(rols[i]);
				if (!(this._oSubject.getPrincipals().contains(oPrincipalRol)))
	            {
                	this._oSubject.getPrincipals().add(oPrincipalRol);
            	}

System.err.println("Rols Usuari.class: " + rols[i]);

			}
            
            // Hem resolt bé el commit:
            this._bCommitSucceded = true;
            return true;
        }
    }
    
    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) 
    {
        // Guardem tots els objectes de forma local al login:
        this._oSubject = subject;
        this._oCallbackHandler = callbackHandler;
        this._oSharedState = sharedState;
        this._oOptions = options;
    }
    
    @Override
    public boolean login() throws LoginException 
    {
        // Primer necessitem afirmar que tenim un objecte CallbackHandler ben
        // configurat i funcionant actualment:
        if (this._oCallbackHandler == null)
        {
            throw new LoginException("ERROR: no hi ha cap CallbackHandler actiu per l'autenticació");
        }
        
        // Creem els objectes específics de callback per usuari i contrassenya:
        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("username");
        callbacks[1] = new PasswordCallback("password: ", false);
        
        try
        {
            // Afegim els nostres callbacks a l'objecte local per aconseguir els
            // valors que s'han passat per la web de login:
            this._oCallbackHandler.handle(callbacks);
            this._sUsuari = ((NameCallback)callbacks[0]).getName();
            this._cContrassenya = ((PasswordCallback)callbacks[1]).getPassword();
            
            // Comprovem que, realment, està funcionant i hem rebut les dades, 
            // com a mínim el nom d'usuari:
            if (this._sUsuari == null)
            {
                throw new LoginException("No es reben les dades de login correctament.");
            }
            
            // Finalment, hem de comprovar si les credencials són correctes:
            Autenticador oAutenticador = new Autenticador(this._sUsuari, this._cContrassenya);
            if (oAutenticador.comprobarCredencials()) 
            {
		// Guardem el llistat de rols abans de continuar:
		this._sRols = oAutenticador.getRols();
                this._bSucceded = true;
                return true;
            }
        }
        catch (IOException | UnsupportedCallbackException e)
        {
            System.err.println("Error: " + e.getMessage());
        }
		catch (LoginException e)
        {
            System.err.println("Error: " + e.getMessage());
        }
        catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());
        }
        
        // En qualsevol altre cas, retornem un false:
        return false;
    }
    
    @Override
    public boolean logout() throws LoginException 
    {
        this._oSubject.getPrincipals().remove(this._oPrincipalUsuari);
        this._bSucceded = false;
        this._sUsuari = null;
        this._cContrassenya = null;
        this._oPrincipalUsuari = null;
        return true;
    }
}
