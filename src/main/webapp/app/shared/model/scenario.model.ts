export interface IScenario {
    id?: number;
    name?: string;
}

export class Scenario implements IScenario {
    constructor(public id?: number, public name?: string) {}
}
