/**
 * Este package contiene clases que sirven para abstraer dependencias con terceros.<br>
 * Todas las librerías que sean tomados de terceros y los objetos de dominio que modelan entidades
 * físicas externas (como archivos) deberían estar en este package para delimitar las dependencias
 * del proyecto.<br>
 * Cad subpackage agregará una capa de abstracción sobre esa dependencia de manera de que sea
 * mockeable en los tests. Por lo tanto toda entidad externa debería ofrecer una interfaz con el
 * sistema de manera de poder utilizar distintas implementaciones de la misma, sin necesidad de
 * salir realmente del sistema
 * 
 */
package net.gaia.vortex.externals;

