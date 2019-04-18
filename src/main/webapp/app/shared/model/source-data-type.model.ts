import { IScenario } from 'app/shared/model/scenario.model';

export interface ISourceDataType {
    id?: number;
    name?: string;
    sourceFileContentType?: string;
    sourceFile?: any;
    scenario?: IScenario;
}

export class SourceDataType implements ISourceDataType {
    constructor(
        public id?: number,
        public name?: string,
        public sourceFileContentType?: string,
        public sourceFile?: any,
        public scenario?: IScenario
    ) {}
}
