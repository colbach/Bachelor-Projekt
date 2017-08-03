package model.type;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import reflection.Function;
import reflection.customdatatypes.SmartIdentifier;
import reflection.customdatatypes.rawdata.RawData;
import reflection.customdatatypes.rawdata.RawDataFromFile;
import reflection.customdatatypes.rawdata.RawDataFromNetwork;
import reflection.nodedefinitionsupport.camera.Camera;
import reflection.customdatatypes.BooleanGrid;
import reflection.customdatatypes.SourceCode;
import reflection.customdatatypes.math.ArrayBasedVector;
import reflection.customdatatypes.math.MathObject;
import reflection.customdatatypes.math.Matrix;
import reflection.customdatatypes.math.NumberMathObject;
import reflection.customdatatypes.math.OneDimensionalArrayBasedMatrix;
import reflection.customdatatypes.math.TwoDimensionalArrayBasedMatrix;
import reflection.customdatatypes.math.Vector;

public class TypeNameTranslation {

    private static TypeNameTranslation germanInstance = null;

    public static TypeNameTranslation getGermanInstance() {
        if (germanInstance == null) {
            HashMap<Class, String> germanTranslationMap = new HashMap<>();
            germanTranslationMap.put(Boolean.class, "Wahrheitswert");
            germanTranslationMap.put(String.class, "Text");
            germanTranslationMap.put(Character.class, "Zeichen");
            germanTranslationMap.put(Number.class, "Zahl");
            germanTranslationMap.put(Byte.class, "8 Bit Ganzzahl");
            germanTranslationMap.put(Short.class, "16 Bit Ganzzahl");
            germanTranslationMap.put(Integer.class, "Ganzzahl");
            germanTranslationMap.put(Long.class, "64 Bit Ganzzahl");
            germanTranslationMap.put(Float.class, "32 Bit Gleitkommazahl");
            germanTranslationMap.put(Double.class, "Gleitkommazahl");
            germanTranslationMap.put(Date.class, "Datum");
            germanTranslationMap.put(Color.class, "Farbe");
            germanTranslationMap.put(Exception.class, "Ausnahme");
            germanTranslationMap.put(Object.class, "Objekt");
            germanTranslationMap.put(Function.class, "Funktion");
            germanTranslationMap.put(Image.class, "Bild");
            germanTranslationMap.put(BufferedImage.class, "Gepuffertes Bild");
            germanTranslationMap.put(RawData.class, "Rohdaten");
            germanTranslationMap.put(RawDataFromFile.class, "Gelesene Rohdaten");
            germanTranslationMap.put(File.class, "Dateipfad");
            germanTranslationMap.put(Path.class, "Pfad"); // Da File automatisch zu Path konvertiert wird ist Verwechselungsgefahr zu vernachlaessigen
            germanTranslationMap.put(RawDataFromNetwork.class, "Empfangene Rohdaten");
            germanTranslationMap.put(Camera.class, "Kamera");
            germanTranslationMap.put(BooleanGrid.class, "Raster");
            germanTranslationMap.put(Matrix.class, "Matrix");
            germanTranslationMap.put(Vector.class, "Vektor");
            germanTranslationMap.put(MathObject.class, "Mathematisches Object");
            germanTranslationMap.put(ArrayBasedVector.class, "Arraybasierter Vektor");
            germanTranslationMap.put(OneDimensionalArrayBasedMatrix.class, "Arraybasierte Matrix");
            germanTranslationMap.put(TwoDimensionalArrayBasedMatrix.class, "2D Arraybasierte Matrix");
            germanTranslationMap.put(NumberMathObject.class, "Zahl");
            germanTranslationMap.put(SourceCode.class, "Quellcode");

            germanInstance = new TypeNameTranslation(germanTranslationMap);
        }
        return germanInstance;
    }

    private final HashMap<Class, String> translationMap;

    private TypeNameTranslation(HashMap<Class, String> translationMap) {
        this.translationMap = translationMap;

    }

    public String get(Class clazz) {
        String get = translationMap.get(clazz);
        if (get == null) {
            String name = clazz.getName();
            int punktPosition = name.lastIndexOf(".");
            if (punktPosition != -1) {
                name = name.substring(punktPosition + 1);
            }
            return name;
        } else {
            return get;
        }
    }
}
