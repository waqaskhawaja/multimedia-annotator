export interface IResourceType {
    id?: number;
    name?: string;
}

export class ResourceType implements IResourceType {
    constructor(public id?: number, public name?: string) {}
}
