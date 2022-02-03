package com.example.mistareas.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Clase utilizada para convertir a resumen hash la password a insertar en la bbdd
 */
public class ConversorHash {
    /**
     * Metodo para convertir las password a resumen hash
     * @param pass la contraseña a convertir en resumen hash
     * @return el resumen hash
     * @throws NoSuchAlgorithmException
     */
    public String convertirAHash(String pass) throws NoSuchAlgorithmException {

        byte[] bytePass = pass.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(bytePass);
        byte[] hash = md.digest();
        String passHash = new String(hash);

        return passHash;
    }

}
