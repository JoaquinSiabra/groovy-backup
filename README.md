# groovy-backup
Script para realizar backups
- Se comprimen con encriptacion los ficheros que comiencen con el "prefijoEncriptables"(en properties)
- Se reencripta el archivo resultante con otra contraseña
- Se mueve este segundo archivo al destino de backup (por ejemplo, Dropbox)
- Se borra el archivo encriptado intermedio
- Se mueve el anterior backup a un directorio de versiones (especificado en properties)

NOTA: es necesario que 7z esté en el PATH 
