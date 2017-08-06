import java.security.Principal;

/**
 *
 * @author Ivan Benaiges Trenchs
 */
public class IDPrincipalUsuari implements Principal
{
    // Definim l'atribut nom:
    private String _sUsuari;
    
    // El constructor sols necessita el nom:
    public IDPrincipalUsuari(String sUsuari)
    {
        if (sUsuari == null)
        {
            // Si no s'ha passat un nom, no permetem que es generi l'objecte i 
            // retornem un error:
            throw new NullPointerException("Nom d'usuari NULL");
        }
        else
        {
            this._sUsuari = sUsuari;
        }
    }
    
    @Override
    public String getName()
    {
        return this._sUsuari;
    }
    
    @Override
    public String toString()
    {
        return this._sUsuari;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this==obj)
        {
            return true;
        }
        
        if (obj instanceof IDPrincipalUsuari)
        {
            IDPrincipalUsuari other = (IDPrincipalUsuari) obj;
            return this._sUsuari.equals(other.getName());
        }
        
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return this._sUsuari.hashCode();
    }
}
