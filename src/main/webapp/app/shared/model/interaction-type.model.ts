export interface IInteractionType {
    id?: number;
    name?: string;
}

export class InteractionType implements IInteractionType {
    constructor(public id?: number, public name?: string) {}
}
