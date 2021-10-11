package Factory;

import Containers.Container;
import Containers.Strategy;

public interface Factory {
    Container createContainer(Strategy strategy);
}
