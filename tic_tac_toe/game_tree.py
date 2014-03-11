# some utilities to explore game trees
import minimax
import copy

class GameStateNode(object):
    
    """Represent a GameState with possible links to successors.
    """

    def __init__(self, game_state, children=None):
        """Create a node to represent game_state.

           Arguments:
           game_state:  A GameState instance of minimax.GameState
        """

        self.value = game_state
        if children is None:
            self.children = []  # populate from game_state.next_move()

    def is_leaf(self):
        """Return True is this TreeNode is a leaf node (has no
        children) and False otherwise."""

        return not self.children
    
    def grow(self):
        """Grow the tree of GameStates, starting from this one.

           Assumptions:
           The tree of GameStates generated from this one is finite, and is
           specified (recursively) by self.value.make_move()
        """
        
        for m in self.value.next_move():
            g = self.value.make_move(m)
            self.children.append(GameStateNode(g))
            self.children[-1].grow()  # grow latest child

def node_count(game_state_node):
    """Return the number of nodes in tree rooted at game_state_node.

       Arguments:
       game_state_node:  A GameStateNode instance.

       Returns:
       Number of nodes in tree rooted at game_state_node.
    """

    # this node, plus kids
    # note the list comprehension
    return 1 + sum([node_count(c) for c in game_state_node.children])

def leaf_count(game_state_node):
    """Return the number of leaves in tree rooted at game_state_node.

       Arguments:
       game_state_node:  A GameStateNode instance.

       Returns:
       Number of childless nodes in tree rooted at game_state_node.
    """
    
    # count the number of leaves in each child
    if game_state_node.is_leaf():
        return 1
    else:
        return sum([leaf_count(c) for c in game_state_node.children])
    
def distinct_node_count(game_state_node):
    """Return number of nodes with distinct layouts in tree rooted at
       game_state_node.

       Arguments:
       game_state_node:  A GameStateNode instance.

       Returns:
       Number of nodes with distinct layouts in tree rooted at
       game_state_node, counting nodes with the same layout only once.
    """
    
    def _mark_distinct_node_states(game_state_node, node_states):
        '''Record distinct nodes in game_state_node.'''
        sig = str(game_state_node.value) # immutable signature
        if sig not in node_states: # never been here
            node_states[sig] = 1 # mark visited
            children = game_state_node.children
            for c in children:
                _mark_distinct_node_states(c, node_states)
    node_states = {}
    _mark_distinct_node_states(game_state_node, node_states)
    return len(node_states.items())
    # count the number of distinct nodes, don't bother exploring subtrees
    # where you have already visited the root!

def distinct_leaf_count(game_state_node):
    """Return number of leaves with distinct layouts in tree rooted at
       game_state_node.

       Arguments:
       game_state_node:  A GameStateNode instance.

       Returns:
       Number of leaves with distinct layouts in the tree rooted at
       game_state_node, that is two leaves with the same state are counted
       only once.
    """

    def _mark_distinct_leaf_states(game_state_node, leaf_states, node_states):
        '''Record distinct leaves and nodes in game_state_node.'''
        sig = str(game_state_node.value) # immutable signature
        if sig not in node_states: # never been here
            node_states[sig] = 1 # mark visited
            children = game_state_node.children
            if not children: # no children, must be a leaf
                leaf_states[sig] = 1 # never been to this leaf
            else:
                for c in children:
                    _mark_distinct_leaf_states(c, leaf_states, node_states)
    node_states = {}
    leaf_states = {}
    _mark_distinct_leaf_states(game_state_node, leaf_states, node_states)
    return len(leaf_states.items())
