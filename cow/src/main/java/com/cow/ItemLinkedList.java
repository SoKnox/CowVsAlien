/**
 * Date: 12/13/24
 * Author: Sophie Knox
 * Class: CRCP3
 * Description: This class represents a linked list of items in the game, which includes methods to insert, remove,
 * and find items, as well as to check if the list is empty and print all items.
 */
package com.cow;

public class ItemLinkedList 
{
    private Item head;

    //constructor
    public ItemLinkedList() 
    {
        this.head = null; //initializes list as empty
    }

    //gets the head of the list
    public Item getHead() 
    {
        return head;
    }

    //prints all the items in the list
    public void print() 
    {
        Item current = head; //starts from the head node
        while (current != null) 
        {
            System.out.println(current.getName()); //prints the item name
            current = current.getNextItem(); //moves to the next node
        }
    }

    //inserts item into a specific index
    public void insert(int index, Item item) 
    {
        if (index == 0) 
        {
            insertAtStart(item);
            return;
        }

        Item current = head; //starts at head node
        int count = 0; //tracks current index

        while (current != null && count < index - 1) 
        {
            current = current.getNextItem(); //moves to the next node
            count++;
        }

        if (current == null) 
        {
            System.out.println("Index out of bounds.");
        } else 
        {
            item.setNextItem(current.getNextItem()); //link the new item to the next item
            current.setNextItem(item); //link the current item to the new item
        }
    }

    //inserts a new item after a specific item
    public void insert(String itemName, Item item) 
    {
        int index = find(itemName);
        if (index != -1) 
        {
            insert(index + 1, item); //inserts the new item after the index
        }
    }

    //inserts item at the start
    public void insertAtStart(Item item) 
    {
        item.setNextItem(head); //item points to current head
        head = item; //item becomes the front of the list
    }

    //inserts item at the end of the list
    public void insertAtEnd(Item item) 
    {
        if (head == null) 
        {
            head = item;
            return;
        }

        Item current = head; //start at head node
        while (current.getNextItem() != null) 
        {
            current = current.getNextItem(); //moves to the next node
        }

        current.setNextItem(item); //puts last item next to the new item
    }

    //checks if the list is empty
    public boolean isEmpty() 
    {
        return head == null; //returns true if the head is null (no nodes in the list)
    }

    //uses name to find index
    public int find(String itemName) 
    {
        Item current = head; //starts at head node
        int index = 0; //tracks current index position

        while (current != null) 
        {
            if (current.getName().equals(itemName)) 
            {
                return index; //returns the item's index
            }
            current = current.getNextItem(); //moves to the next node
            index++; //increment the index
        }

        return -1; //return -1 if the item is not found in the list
    }

    //removes a specific item
    public void remove(String itemName) 
    {
        if (head == null) 
        {
            System.out.println("List is empty.");
            return;
        }

        if (head.getName().equals(itemName)) 
        {
            head = head.getNextItem(); //moves head to the next node
            return;
        }

        Item current = head; //starts at head node
        while (current.getNextItem() != null && !current.getNextItem().getName().equals(itemName)) 
        {
            current = current.getNextItem(); //moves to the next node
        }

        if (current.getNextItem() == null) 
        {
            System.out.println("Item not found."); //debugging
        } else 
        {
            current.setNextItem(current.getNextItem().getNextItem()); //skips the node (removes it)
        }
    }
}

