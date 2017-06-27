/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _testing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author christiancolbach
 */
public class RegexTest {
    public static void main(String[] args) {
        String subject = "He is a \"man of his\" words\\ always 'and forever'";
        Pattern pattern = Pattern.compile( "(?i)((?:(['|\"]).+\\2)|(?:\\w+\\\\\\s\\w+)+|\\b(?=\\w)\\w+\\b(?!\\w))" );
        Matcher matcher = pattern.matcher( subject );
        while( matcher.find() ) {
            System.out.println( matcher.group(0).replaceAll( subject, "$1" ));
        }
    }
}
