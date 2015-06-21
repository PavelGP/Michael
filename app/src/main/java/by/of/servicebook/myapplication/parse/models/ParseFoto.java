package by.of.servicebook.myapplication.parse.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import by.of.servicebook.myapplication.db.models.Detail;

/**
 * Created by Pavel on 25.05.2015.
 */
@ParseClassName("Files")
public class ParseFoto extends ParseObject {
    public static final String FILE = "file";

    public ParseFoto(){}

    public ParseFoto(ParseFile parseFile){
        put(FILE, parseFile);
    }

}
