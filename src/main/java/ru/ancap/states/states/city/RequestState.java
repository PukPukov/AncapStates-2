package ru.ancap.states.states.city;

public sealed interface RequestState<REQUESTER> permits RequestState.Instruction, RequestState.Request {

    static <REQUESTER> RequestState<REQUESTER> INSTRUCTION() {
        return new RequestState.Instruction<>();
    }

    static <REQUESTER> RequestState<REQUESTER> REQUEST(REQUESTER requester) {
        return new RequestState.Request<>(requester);
    }

    final class Instruction<REQUESTER> implements RequestState<REQUESTER> {
        
    }

    record Request<REQUESTER>(REQUESTER requester) implements RequestState<REQUESTER> {
        
    }
    
}
