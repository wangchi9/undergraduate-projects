"""Test the pqueue module."""

import unittest
import media
from priorityqueue import PriorityQueue
from rectangle import Rectangle
from oval import Oval

class PQueueTestCase(unittest.TestCase):
    '''Test creation and use of a priority queue.'''
    
    def setUp(self):
        self.pq = PriorityQueue()
        self.r1 = Rectangle(0, 150, 200, 50, media.forestgreen, 1)
        self.o1= Oval(120, 160, 60, 60, media.white, 4)
        self.o2 = Oval(120, 115, 45, 45, media.yellow, 3)
        self.o3 = Oval(120, 80, 30, 30, media.orange, 2)

    def tearDown(self):
        self.pq = None
        self.r1 = None
        self.o1 = None
        self.o2 = None
        self.o3 = None
            
    def testSize(self):
        assert self.pq.size() == 0, 'mismatch in predicted pqueue size'
        
    def testEnqueueSize(self):
        self.pq.enqueue(self.r1)
        assert self.pq.size() == 1, \
               'mismatch in pqueue size after enqueue'

    def testDequeue(self):
        self.pq.enqueue(self.r1)
        result = self.pq.dequeue()
        assert result is self.r1, \
               'mismatch in dequeued value'

    def testMultipleEnqueueSize(self):
        self.pq.enqueue(self.o1)
        self.pq.enqueue(self.o2)
        self.pq.enqueue(self.o3)
        assert self.pq.size() == 3, \
               'mismatch in pqueue size after enqueues'

    def testMultipleDequeue(self):
        self.pq.enqueue(self.o1)
        self.pq.enqueue(self.o2)
        self.pq.enqueue(self.o3)
        result = self.pq.dequeue()
        assert result is self.o3, 'mismatch in dequeuing correct value'


def pqueue_suite():
    return unittest.TestLoader().loadTestsFromTestCase(PQueueTestCase)

runner = unittest.TextTestRunner()
runner.run(pqueue_suite())
