%%
%ignorecase
%public
%class PseintLexer
%unicode
%standalone

%{
public static final int PALABRA_RESERVADA = 1;
public static final int OPERADOR = 2;
public static final int SIMBOLO = 3;
public static final int COMENTARIO = 4;
public static final int NUMERO = 5;
public static final int SALTO_LINEA = 6;
public static final int CADENA = 7;
public static final int OTRO = 8;
%}

%%
// Palabras reservadas en PSeInt
"algoritmo" | "proceso" | "finproceso" | "subproceso" | "finsubproceso" |
"funcion" | "finfuncion" | "si" | "finsi" | "sino" | "mientras" |
"finmientras" | "para" | "finpara" | "hacer" | "segun" | "finsegun" |
"caso" | "defecto" | "escribir" | "leer" | "verdadero" | "falso" |
"entero" | "real" | "logico" | "caracter" | "cadena" | "no" | "y" | "o" | "mod" |
"limpiar" | "esperar" | "pantalla" | "milisegundos" | "como" | "definir"
    { return PALABRA_RESERVADA; }

// Operadores
"+" | "-" | "*" | "/" | "=" | "<" | ">" | "<=" | ">=" | "<>" | "!="
    { return OPERADOR; }

// Símbolos y delimitadores
"(" | ")" | "[" | "]" | "{" | "}" | ";" | "," {
    System.out.println("Símbolo encontrado: " + yytext());
    return SIMBOLO;
}

// Comentarios
"//".* { return COMENTARIO; }

// Cadenas de texto entre comillas dobles
\"[^\"]*\" {
    System.out.println("Cadena encontrada: " + yytext());
    return CADENA;
}

// Números (enteros o decimales)
[0-9]+(\.[0-9]+)? { return NUMERO; }

// Saltos de línea
\n { return SALTO_LINEA; }

// Ignorar espacios y tabulaciones
[ \t\r]+ { /* No hacer nada */ }

// Cualquier otro texto no reconocido
[a-zA-Z_][a-zA-Z0-9_]* { return OTRO; }
