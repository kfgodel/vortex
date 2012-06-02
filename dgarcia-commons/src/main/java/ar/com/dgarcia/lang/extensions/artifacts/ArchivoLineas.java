/*
 * Created on 15-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ar.com.dgarcia.lang.extensions.artifacts;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author D. Garcia
 *
 * Encapsula un archivo, representandolo como un contenedor de
 * lineas que son leidas una tras otra
 */
public class ArchivoLineas {
    /**
     * Error producido durante la
     * operacion
     */
    private Exception error;
    
    /**
     * Flujo desde el que se lee el archivo
     */
    private BufferedReader flujo;
    
    /**
     * Constructor que recibe un path
     * para acceder al archivo
     * @param path String con la ubicacion
     * del archivo
     * @throws FileNotFoundException
     * si el archivo no es accesible
     */
    public ArchivoLineas(String path) throws FileNotFoundException{
        FileInputStream archivo = new FileInputStream(path);
        this.flujo = new BufferedReader(new InputStreamReader(archivo));
    }
    
    /**
     * Cierra el archivo, registrando
     * el error que se produzca
     */
    public void cerrar(){
        try {
            this.flujo.close();
        } catch (IOException e) {
            this.error = e;
        }
    }
    
    /**
     * Lee la proxima linea de texto del archivo abierto 
     * @return Un String con la linea del archivo leida. 
     * null si se produjo un error
     */
    public String leerLinea(){
        try {
            return this.flujo.readLine();
        } catch (IOException e) {
            this.error = e;
            return null;
        }
    }
    
    /**
     * Indica si se puede seguir leyendo del archivo. 
     * @return false si se lleg� al final o se perdi� el 
     * acceso al archivo
     */
    public boolean puedeLeer(){
        if(this.error != null)
            return false;
        try {
            return this.flujo.ready();
        } catch (IOException e) {
            this.error = e;
            return false;
        }
    }
}
