import java.security.Principal;

/**
 *
 * @author Ivan Benaiges Trenchs
 */
public class IDPrincipalRol implements Principal
{
    // Definim l'atribut nom:
    private String _sRol;
    
    // El constructor sols necessita el nom:
    public IDPrincipalRol(String sRol)
    {
        if (sRol == null)
        {
            // Si no s'ha passat un nom, no permetem que es generi l'objecte i 
            // retornem un error:
            throw new NullPointerException("Nom d'usuari NULL");
        }
        else
        {
            this._sRol = sRol;
        }
    }
    
    @Override
    public String getName()
    {
        return this._sRol;
    }
    
    @Override
    public String toString()
    {
        return this._sRol;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this==obj)
        {
            return true;
        }
        
        if (obj instanceof IDPrincipalRol)
        {
            IDPrincipalRol other = (IDPrincipalRol) obj;
            return this._sRol.equals(other.getName());
        }
        
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return this._sRol.hashCode();
    }
}
