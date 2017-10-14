# This contains the Neural Network system's source files


# Basic architecture of the neural-network library.

Unlike popular implementations that focus on developing mathematical computation modules or systems that support matrix mathematics which were used in mathematically representing the neural network system to feed-forward and back-propagate, this implementation contains a dynamically constructed graph which plainly represents the structure of the neural network system. The implementation is structured similar to a data-flow graph, but neither contains support for parallel computing nor supports GPU accelerators. 

Each node in the graph here, represents an individual neuron. Libraries like TensorFlow implement a data-stream graph architecture, in which each node contains a single computation. But this library constructs a graph which literally define the architecture of the neural-network system, with each neuron being a node. The connections between nodes in the graph define how data flows. An implementation structured this way allows us to experiment with various architectures and also implement any kind of abstract architecture that might prove to be beneficial.    
