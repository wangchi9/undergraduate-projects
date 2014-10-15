import media
from priorityqueue import PriorityQueue
from oval import Oval
from rectangle import Rectangle
from line import Line

class Collage(PriorityQueue, Oval, Rectangle, Line):
    '''
    Using the shape specifications outlined in the data file, create a 
    collage of shapes to form a complete picture. Create the collage image 
    and a priority queue structure. to store all the shapes that go into the 
    final image.
    the class has methods:
    __init__
    get_picture
    show
    and help method: shape_class
    '''
    
    def __init__(self, filename):
        '''
        Using the shape specifications outlined in the data file, create 
        a collage of shapes to form a complete picture. The data file location 
        is specified by the str filename. You can assume that this string 
        contains the location of a valid file. 
        In order to create the collage image, you'll need to create a 
        priority queue structure first, to store all the shapes that go into
        the final image. Once all the shapes have been enqueued, the 
        collage can be created by creating a new image 
        (using media.create_picture()), and removing the Shape objects
        from the priority queue one at a time, drawing them as they're 
        being dequeued. 
        '''
        
        data = open(filename, 'r').readlines()
        
        self.pic = create_pic(data)
        pqueue = PriorityQueue()
        i = 1
        while i < len(data):
            shape_list = data[i].split(', ')
            pqueue.enqueue(self.shape_class(shape_list))
            i += 1
        
        while pqueue.lst != []:
            shape = pqueue.dequeue()    
            self.pic = shape.draw(self.pic)
        
    def get_picture(self):
        '''Return the Picture that stores the assembled collage. '''
        
        return self.pic
    
    def show(self):
        '''
        Display the collage in a local picture-viewing window 
        (invoke the Picture's show() method) 
        '''
        
        media.show(self.pic)
    
    # help methods for __init__:
    def shape_class(self, shape_list):
        '''
        Return a class of shape by information form a list. The list is a 
        line form Data.
        '''  
        
        x = int(shape_list[1])
        y = int(shape_list[2])
        w = int(shape_list[3])
        h = int(shape_list[4])
        col = convert_colour(shape_list[5])
        pqueue = int(shape_list[6].split()[0])
        
        if shape_list[0] == 'oval':            
            clas = Oval(x, y, w, h, col, pqueue)  
            
        elif shape_list[0] == 'line':
            clas = Line(x, y, w, h, col, pqueue)  
            
        else:
            clas = Rectangle(x, y, w, h, col, pqueue)       
            
        return clas
    
def create_pic(data):
    '''
    Return a created picture by the first element in data list.
    '''
    
    pic_list = data[0].split(', ')
    col = convert_colour(pic_list[2].split()[0])
    
    pic = media.create_picture(int(pic_list[0]), int(pic_list[1]), col)
    
    return pic
    
def convert_colour(colour_string):
    '''Convert the colour named in the input string into a Color object
    by looking up the entry for this colour in the media dictionary.'''
    
    return media.__dict__.get(colour_string) 