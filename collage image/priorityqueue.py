import media
from oval import Oval
from rectangle import Rectangle
from line import Line

class PriorityQueue(Oval, Rectangle, Line):
    '''
    The priority queue ADT is an enhanced version of the Queue ADT that assumes 
    that the elements in the queue store both a data value and a priority 
    value for each element.
    the class has methods:
    __init__
    enqueue
    dequeue
    size
    __str__
    '''
    
    def __init__(self, lst=None):
        '''
        Initialize this priority queue with the given list of items. 
        All of the items in the list will inherit from Shape, so they 
        will all have the get_priority and set_priority methods. However, 
        you cannot assume that the list is ordered in any way. The default 
        value for the list parameter should be the empty list. 
        '''
        if lst is None:
            self.lst = []
        else:
            self.lst = lst
          
    def enqueue(self, item):
        '''Add an element to the priority queue. '''
        
        self.lst.append(item)
    
    def dequeue(self):
        '''
        Remove the element with the lowest priority value from the
        priority queue, and return this element. If two elements have 
        the same priority value, remove the one that was added first 
        to the priority queue. 
        '''

        p = self.lst[0].get_priority()
        clas = self.lst[0]
        for c in self.lst:
            if c.get_priority() < p:
                p = c.get_priority()
                clas = c
                
        self.lst.remove(clas)
        return clas
       
    def size(self):
        '''
        Return the size of this priority queue (the number of elements 
        currently stored in the queue). 
        '''
        
        return len(self.lst)
    
    def __str__(self):
        '''
        Return the contents of this priority queue as a string. This string 
        is composed of the string version of each item within the queue, in 
        the priority queue order specified above, with a newline character 
        (\n) separating each item.
        For instance, if the __str__ method is called from a priority queue 
        with an Oval, a Rectangle and a Line.
        '''
        
        clas_list = self.lst
        string = ''
        while clas_list != []:
            clas = clas_list.self.dequeue()
            clas_str = clas + ', priority = ' + clas.get_priority() + '\n'
            string += clas_str
            
        return string 