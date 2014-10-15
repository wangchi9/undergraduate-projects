import media
from shape import Shape

class Line(Shape):
    '''
    Draw a Line form (x,y) to (end_x,end_y). 
    the class has methods:
    __init__
    __str__
    draw
    '''
    
    def __init__(self, x=0, y=0, end_x=0, end_y=0, col=media.white, 
                 priority=0):
        '''
        Initialize the attributes of the Line object. The x and y 
        values represent the starting point of the line, and the x_end 
        and y_end values represent the endpoint of the line. Both pairs of 
        x and y values are passed in as integers, as is the priority value. 
        As with Oval and Rectangle, the colour parameter is a media.color 
        value. 
        '''
        
        self.x = x
        self.y = y
        self.end_x = end_x
        self.end_y = end_y
        self.col = col
        self.priority = priority
               
    def __str__(self):
        '''
        Returns the starting and ending coordinates of this Line, as a 
        string with the following formatting: 
        Line @ ( 50 , 75 ) to ( 20 , 30 )
        '''
        
        str1 = "Line @ ( " + str(self.x) + " , " + str(self.y) + " ) "
        str2 = "to ( " + str(self.end_x) + " , " + str(self.end_y) + " )"
        
        return str1 + str2
    
    def draw(self, pic):
        '''
        Draw this Line on the Picture provided. Use the data attributes of
        this Line to specify the starting and ending positions of the 
        resulting line, with the Line's colour value and a thickness of one. 
        '''
        
        media.add_line(pic, self.x, self.y, self.end_x, self.end_y, self.col)
        
        return pic   