package tp4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.lang.Math;

public class Heap<ValueType extends Comparable<? super ValueType>> implements Iterable<ValueType> {
    private ArrayList<ValueType> data;
    private boolean isMin;

    // O(1)
    public Heap() {
        this(true);
    }

    // O(1): construction sans donnees initiales
    public Heap(boolean isMin) {
        // TODO
        this.isMin = isMin;
        data = new ArrayList<ValueType>();
    }

    // O(n)
    public Heap(Collection<ValueType> data) {
        this(true, data);
    }

    // O(n): construction avec donnees initiales, allez voir le lien dans la description pour vous aider
    public Heap(boolean isMin, Collection<ValueType> data) {
        // TODO
        this(isMin);
        this.data.add(null);    // Heap qui commence à l'index 1 donc null à la position 0
        this.data.addAll(data); //O(n)
        build();

    }

    // O(1): on retourne le nombre d'elements dans la liste
    public int size() {
        // TODO
        return data.size()-1;
    }

    // O(1): on compare deux elements en fonction du type de monceau
    private boolean compare(ValueType first, ValueType second) {
        // TODO
        return isMin ? first.compareTo(second) < 0 : first.compareTo(second) > 0;
    }

    // O(1): on donne l'indice du parent
    private int parentIdx(int idx) {
        // TODO
        return idx/2;
    }

    // O(1): on donne l'indice de l'enfant de gauche
    private int leftChildIdx(int idx) {
        // TODO
        return idx*2;
    }

    // O(1): on donne l'indice de l'enfant de droite
    private int rightChildIdx(int idx) {
        // TODO
        return (idx*2)+1;
    }

    // O(1): on echange deux elements dans le tableau
    private void swap(int firstIdx, int secondIdx) {
        // TODO
        if(firstIdx != secondIdx){
            ValueType temp = data.get(secondIdx);
            data.set(secondIdx, data.get(firstIdx));
            data.set(firstIdx, temp);
        }

    }

    // O(log(n)): l'index courant represente le parent, on s'assure que le parent soit le min/max avec ses enfants
    // De facon visuelle, ceci ammene un noeud le plus haut possible dans l'arbre
    // Par exemple: si le min/max est une feuille, on appelera resursivement log(n) fois la methode pour monter le noeud
    private void heapify(int idx) {
        // TODO
        // ...
        if(idx>1){
            if(!compare(data.get(parentIdx(idx)), data.get(idx))){
                swap(parentIdx(idx), idx);
                heapify(parentIdx(idx));
            }
        }

    }

    // O(log(n)): on ajoute un element et on preserve les proprietes du monceau
    public void insert(ValueType element) {
        // TODO
        if(data.size()==0)
            data.add(0,null);   // Heap qui commence à l'index 1 donc null à la position 0
        data.add(element);
        heapify(data.indexOf(element));
    }

    // O(n): on s'assure que tous les elements sont bien places dans le tableau,
    // allez voir le lien dans la description pour vous aider
    public void build() {
        // TODO
        for(int i=1; i<=size(); i++){
            heapify(i);
        }
    }

    // O(log(n)) On fait redescendre l'élement le plus bas possible de sorte à maintenir les propriétées du heap
    // Géneralement appelé avec hole = 1 (la racine)
    public void percolateDown(int hole){
        int child;
        ValueType tmp = data.get(hole);
        for( ; hole * 2 < data.size(); hole = child )
        {
            child = leftChildIdx(hole); //Considérer fils de gauche

            // Si il y a deux fils et fils droit<fils gauche(minHeap) ou droit>gauche(maxHeap)
            if( child != size() && compare(data.get(child+1),data.get(child))){
                child++; //Considérer fils droit
            }

            if(compare(data.get(child), tmp))
                swap(hole,child);
            else
                break; //sortir de la boucle. L’élément à percoler sera inséré à position hole
        }


    }

    // O(log(n)): on retire le min ou le max et on preserve les proprietes du monceau
    public ValueType pop() {
        // TODO
        ValueType toRemove = peek();
        swap(1,size());
        data.remove(size());
        if(size()>=1)
            percolateDown(1);

        return toRemove;
    }

    // O(1): on retourne sans retirer le plus petit ou plus grand element.
    public ValueType peek() {
        // TODO
        return data.get(1);
    }

    // O(nlog(n)): On applique l'algorithme Heap Sort, on s'attend a ce que le monceau soit vide a la fin.
    public List<ValueType> sort() {
        // TODO
        ArrayList<ValueType> result = new ArrayList<ValueType>();
        while (size()>1){
            result.add(pop());
        }
        result.add(peek()); // On ajout le dernier élement restant dans le monceau
        return result;
    }

    // Creation d'un iterateur seulement utilise dans les tests, permet de faire des boucles "for-each"
    @Override
    public Iterator<ValueType> iterator() {
        // TODO
        return new Iterator<ValueType>() {
            private int current = 0;

            @Override
            public boolean hasNext() {
                return (current+1 <= size());
            }

            @Override
            public ValueType next() {
                if(hasNext()){
                    current++;
                    return data.get(current);
                }
                return null;
            }
        };
    }
}
