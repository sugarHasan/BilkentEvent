/*
This java class is for creating a box of events
Created by Hasan Yildirim
20.February.2019
 */
package com.example.bilkentevent;



public class EventBox {
    //constants
    final private int MAX_SIZE = 1000000;

    //variables

    private Event[] heap;
    private int size;

    public EventBox(){
        size = 0;
        heap = new Event[MAX_SIZE];
    }

    public void addEvent(Event element) {
        heap[size] = element;
        size++;
    }
    public Event getEvent(int i ){
        return heap[i];
    }

    public int getMAX_SIZE() {
        return MAX_SIZE;
    }

    public Event[] getHeap() {
        return heap;
    }

    public void setHeap(Event[] heap) {
        this.heap = heap;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
