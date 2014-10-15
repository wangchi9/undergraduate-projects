"""A game trie in a game of Ghost.

A game trie is a player's mental image of the game. The data of a
TrieNode in this trie is True if this node is winning and False
otherwise.

GameTrie supports the following operations (in addition to those of
Trie):
 -- is_winning(node): return whether the TrieNode 'node' is winning.
"""
import copy
from trie import Trie, TrieNode

class GameTrie(Trie):
    """A game trie in a game of Ghost. GameTrie has the following
    attributes:
    -- _num_players: the number of players in the game.
    -- _root: the root TrieNode.
    """

    def __init__(self, start_node, num_players):
        """Create and return a GameTrie that corresponds to a lexicon
        subtrie rooted at node 'start_node', for a game with
        'num_players' players.

        -- start_node: a TrieNode in a LexTrie. The resulting GameTrie
        corresponds to a subtrie rooted at this node.
        -- num_players: the number of players in this game.

        Assumptions:
        -- 'start_node' is not a leaf node.
        -- 'num_players' >= 2.
        -- It is this player's turn in the game.
        """

        Trie.__init__(self)
        self._num_players = num_players
        # build the game tree
        players = num_players
        # start from me
        self._root = copy.deepcopy(start_node)
        set_tree_again(self._root, 1, num_players)
        
    def is_winning(self, node):
        """Return True if TrieNode 'node' is winning."""

        return node.data()

def set_tree_again(node, num_players, players):
    '''   
    '''
    
    if node._children:
        for child in node._children.keys():
            if num_players == 1:
                set_tree_again(node._children[child], players, players)
            else:
                set_tree_again(node._children[child], num_players-1, players)
                
        if num_players == players:        # me 
            if node._data:
                node._data = None
            else:
                for child in node._children.keys():
                    if node._children[child]._data:
                        node._data = 'is winning'
        else:     # others
            for child in node._children.keys():
                if node._children[child]._data:
                    node._data = 'is winning'
    
    else:    
        if num_players == players:
            if node._data:
                node._data = None
            else:
                node._data = 'is winning'

        else:
            if node._data:
                node._data = 'is winning' 