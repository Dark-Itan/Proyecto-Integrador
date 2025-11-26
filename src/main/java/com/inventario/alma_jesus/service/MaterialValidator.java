package com.inventario.alma_jesus.service;

import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;

public class MaterialValidator {

    private static final String[] MATERIALES_ENTEROS = {
            "pincel", "brocha", "lija", "espátula", "clavo", "tornillo",
            "destornillador", "martillo", "taladro", "sierra", "cutter",
            "rodillo", "guante", "mascarilla", "lente"
    };

    private static final String[] UNIDADES_FRACCIONABLES = {
            "litro", "kg", "kilo", "gramo", "metro", "cm", "mm", "ml", "centimetro"
    };

    public static Map<String, Object> parseMaterialWithQuantity(String input) {
        Map<String, Object> result = new HashMap<>();
        if (input == null || input.trim().isEmpty()) return result;

        String trimmed = input.trim().toLowerCase();
        System.out.println("🔍 [MATERIAL-VALIDATOR] Analizando: " + trimmed);

        boolean esMaterialEntero = esMaterialEntero(trimmed);
        Pattern fractionPattern = Pattern.compile("\\b\\d+/\\d+\\b");
        Pattern decimalPattern = Pattern.compile("\\b\\d+\\.?\\d*\\b");

        try {
            java.util.regex.Matcher fractionMatcher = fractionPattern.matcher(trimmed);
            if (fractionMatcher.find()) {
                String fraccion = fractionMatcher.group();
                if (esMaterialEntero) {
                    throw new IllegalArgumentException("Material '" + obtenerNombreMaterial(trimmed) + "' no acepta fracciones. Use números enteros.");
                }
                String[] parts = fraccion.split("/");
                double numerator = Double.parseDouble(parts[0]);
                double denominator = Double.parseDouble(parts[1]);
                if (denominator == 0) throw new IllegalArgumentException("Denominador no puede ser cero");
                double cantidad = numerator / denominator;
                result.put("cantidad", cantidad);
                result.put("tipo", "fraccion");
                result.put("esEntero", false);
                System.out.println("🔢 Convertido " + fraccion + " a " + cantidad);
            } else if (decimalPattern.matcher(trimmed).find()) {
                java.util.regex.Matcher decimalMatcher = decimalPattern.matcher(trimmed);
                if (decimalMatcher.find()) {
                    String numeroStr = decimalMatcher.group();
                    double cantidad = Double.parseDouble(numeroStr);
                    if (esMaterialEntero && cantidad % 1 != 0) {
                        throw new IllegalArgumentException("Material '" + obtenerNombreMaterial(trimmed) + "' requiere cantidad entera. No use decimales.");
                    }
                    result.put("cantidad", cantidad);
                    result.put("tipo", cantidad % 1 == 0 ? "entero" : "decimal");
                    result.put("esEntero", esMaterialEntero);
                    System.out.println("🔢 Número: " + cantidad + " (entero: " + esMaterialEntero + ")");
                }
            }
            String nombreMaterial = obtenerNombreMaterial(trimmed);
            result.put("nombreMaterial", nombreMaterial);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato inválido. Use números o fracciones válidas");
        }
        return result;
    }

    private static boolean esMaterialEntero(String texto) {
        for (String material : MATERIALES_ENTEROS) {
            if (texto.contains(material)) {
                System.out.println("✅ Material entero detectado: " + material);
                return true;
            }
        }
        return false;
    }

    private static String obtenerNombreMaterial(String texto) {
        String nombre = texto.replaceAll("\\b\\d+/\\d+\\b", "")
                .replaceAll("\\b\\d+\\.?\\d*\\b", "")
                .replaceAll("\\b(litro|kg|kilo|gramo|metro|cm|mm|ml|centimetro)\\b", "")
                .replaceAll("\\s+", " ")
                .trim();
        return nombre.isEmpty() ? "material varios" : nombre;
    }

    public static void procesarMaterialesParaInventario(String textoMateriales) {
        if (textoMateriales == null || textoMateriales.trim().isEmpty()) return;

        System.out.println("\n🎨 [MATERIAL-VALIDATOR] Procesando materiales: " + textoMateriales);
        String[] materiales = textoMateriales.split(",");

        for (String material : materiales) {
            String materialTrim = material.trim();
            try {
                Map<String, Object> resultado = parseMaterialWithQuantity(materialTrim);
                if (!resultado.isEmpty()) {
                    String nombre = (String) resultado.get("nombreMaterial");
                    Double cantidad = (Double) resultado.get("cantidad");
                    Boolean esEntero = (Boolean) resultado.get("esEntero");
                    System.out.println("✅ MATERIAL: " + nombre);
                    System.out.println("   📦 Cantidad: " + cantidad + " (" + (esEntero ? "ENTERO" : "FRACCION") + ")");
                }
            } catch (IllegalArgumentException e) {
                System.err.println("❌ Error en material '" + materialTrim + "': " + e.getMessage());
                throw e;
            }
        }
        System.out.println("✅ [MATERIAL-VALIDATOR] Procesamiento completado\n");
    }

    public static void validarMaterialesUsados(String materialesUsados) {
        if (materialesUsados == null || materialesUsados.trim().isEmpty()) return;

        System.out.println("🔍 [MATERIAL-VALIDATOR] Validando materiales...");
        String[] materiales = materialesUsados.split(",");

        for (String material : materiales) {
            String materialTrim = material.trim();
            try {
                Map<String, Object> resultado = parseMaterialWithQuantity(materialTrim);
                if (resultado.isEmpty()) {
                    System.out.println("⚠️ Material sin cantidad específica: " + materialTrim);
                }
            } catch (IllegalArgumentException e) {
                System.err.println("❌ Validación fallida: " + e.getMessage());
                throw new IllegalArgumentException("Error en material '" + materialTrim + "': " + e.getMessage());
            }
        }
        System.out.println("✅ [MATERIAL-VALIDATOR] Validación completada exitosamente");
    }
}