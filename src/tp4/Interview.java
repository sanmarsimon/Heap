package tp4;

import com.sun.jdi.Value;
import com.sun.source.doctree.AttributeTree;

import javax.swing.text.html.HTMLDocument;
import java.lang.reflect.Array;
import java.util.*;

public final class Interview {
    /**
     * @param circleSize le nombre d'amis que l'on doit inclure dans le cercle
     * @param centers les indices des centres dans "points"
     * @param points la liste des individus
     * @return les indices dans "points" qui creent l'intersection de deux cercles d'amis.
     *
     * On veut que vous indiquiez la complexite sur chaque ligne en fonction de:
     *  - circleSize -> O(a) a est pour le nombre d' "amis" communs
     *  - centers -> O(c) c est pour le nombre de "centres"
     *  - points -> O(n) n est simplement le nombre de d'individus dans la population
     * Vous n'avez pas besoin d'indiquer la complexite des lignes etant en O(1)
     * Lorsque vous avez des boucles, indiquez clairement la complexite de la boucle, par exemple:
     *   for (Integer p1 : points) { // O(n) * O(n^2*log(n)) -> O(n^3*log(n))
     *     for (Integer p2 : points) { // O(n) * O(n*log(n) -> O(n^2*log(n))
     *       Collections.sort(points); // O(n*log(n)
     *     }
     *   }
     * Ceci est un exemple, on voit clairement que la boucle sur "p2" est en O(n) et puisque son interieur est
     * en O(n*log(n), la complexite totale sera la multiplication, O(n^2*log(n)).
     *
     * O(n^2 * log(n)): ceci est la complexite en "worst case" ou 'a' et 'c' tendent vers 'n'
     * Il est peut etre possible d'avoir une meilleure complexite, soyez clair dans vos explications si vous croyez
     * avoir trouve :)
     */
    public static List<Integer> getFriendsToRemove(Integer circleSize, List<Integer> centers, List<Point> points) {
        List<Point> friendsToRemove = new ArrayList<>();
        List<Integer> toRemove = new ArrayList<>();
        PriorityQueue<Point> pointsInCircle = new PriorityQueue<>(circleSize, Point::compareTo);
        PriorityQueue<Point> checkForOverlap = new PriorityQueue<>(circleSize, Point::compareTo);

        for(int i = 0; i < centers.size(); i++){
            Integer centerPos = centers.get(i);
            for(int j = 0; j < points.size(); j++){
                points.get(j).calculateDistance(points.get(centerPos));
                points.get(j).setPos(j);
                if(j!=centerPos.intValue())
                    pointsInCircle.add(points.get(j));
            }

            if(pointsInCircle.size() >= circleSize){
                for(int k = 0; k < circleSize; k++){
                    Point point1 = pointsInCircle.poll();
                    if(checkForOverlap.contains(point1)){
                        if(!friendsToRemove.contains(point1))
                            friendsToRemove.add(point1);
                    }
                    else{
                        checkForOverlap.add(point1);
                    }
                }
                pointsInCircle.clear();
            }
        }

        for(int k = 0; k < friendsToRemove.size(); k++){
            toRemove.add(friendsToRemove.get(k).getPos());
        }

        Collections.sort(toRemove);


        return toRemove;
    }
}

/*
        Point[] arr = new Point[heap.size()];
        Point[] arr1 = heap.toArray(arr);
        PriorityQueue<Point> sort = new PriorityQueue<>(circleSize, Point::compareTo);
        List<Integer> toRemove = new ArrayList<>();
        for(int i = 0; i < arr1.length; i++){
            for(int j = 0; j < friendsToRemove.size(); j++){
                if(arr1[i].getPos() == friendsToRemove.get(j)){
                    sort.add(arr[i]);
                }
            }
        }
        Point[] arr2 = new Point[sort.size()];
        Point[] arr3 = sort.toArray(arr2);
        for(int i = 0; i < arr3.length; i++){
            toRemove.add(arr3[i].getPos());
        }
 */