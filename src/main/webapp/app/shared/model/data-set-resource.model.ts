export interface IDataSetResource {
    id?: number;
    name?: string;
    sourceFileContentType?: string;
    sourceFile?: any;
}

export class DataSetResource implements IDataSetResource {
    constructor(public id?: number, public name?: string, public sourceFileContentType?: string, public sourceFile?: any) {}
}
