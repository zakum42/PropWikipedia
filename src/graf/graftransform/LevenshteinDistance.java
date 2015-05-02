package graf.graftransform;

/**
 * Created by gus on 02/05/15.
 */
public class LevenshteinDistance {
    
    public static int calculate(String s1, String s2){
        if (s1 == s2) return 0;
        if (s1.length() == 0) return s2.length();
        if (s2.length() == 0) return s1.length();

        int[] v0 = new int[s2.length() + 1];
        int[] v1 = new int[s2.length() + 1];

        for (int i = 0; i < v0.length; i++)
            v0[i] = i;

        for (int i = 0; i < s1.length(); i++)
        {

            v1[0] = i + 1;


            for (int j = 0; j < s2.length(); j++)
            {
                int cost = (s1.charAt(i) == s2.charAt(j)) ? 0 : 1;
                v1[j + 1] = Math.min(Math.min(v1[j] + 1, v0[j + 1] + 1), v0[j] + cost);
            }

            System.arraycopy(v1, 0, v0, 0, v0.length);
        }

        return v1[s2.length()];
    }
}
