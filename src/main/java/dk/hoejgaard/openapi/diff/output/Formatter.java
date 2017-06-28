package dk.hoejgaard.openapi.diff.output;

/**
 * a set of very specific formatting utilities for the renders
 */
class Formatter {

    private Formatter() { /*empty*/ }

    /**
     * outputs an observation found where . are replaced with spaces and first letter of words upper cased
     * @param incident the observation to be formatted
     */
    static String out(String incident) {
        String result = incident.replace('.', ' ');
        result = toUpperCamelCase(result);
        return result;
    }

    /**
     * outputs an observation found in the existing API
     * @param incident the observation to be formatted
     */
    static String outX(String incident) {
        int index = incident.indexOf("existing.compliance");
        int indexFor = incident.indexOf("existing.compliance.for");
        int indexObservation = incident.indexOf(".observation");
        String result;
        if ((indexFor >= 0) && (indexObservation >= 0) && indexFor + 23 < incident.length()) {
            result = incident.substring(indexFor + 23, indexObservation);
        } else if (index >= 0 && index + 19 < incident.length()) {
            result = incident.substring(index + 19, incident.length());
        } else {
            result = incident;
        }
        result = result.replace('.', ' ');
        result = toUpperCamelCase(result);
        return result;
    }

    private static String toUpperCamelCase(String input) {
        String[] words = input.trim().split("\\W");
        for (String word : words) {
            if (word.length() < 1) {
                return input;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1, word.length())).append(" ");
        }
        return sb.toString();
    }

}
