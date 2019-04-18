/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { SourceDataTypeComponent } from 'app/entities/source-data-type/source-data-type.component';
import { SourceDataTypeService } from 'app/entities/source-data-type/source-data-type.service';
import { SourceDataType } from 'app/shared/model/source-data-type.model';

describe('Component Tests', () => {
    describe('SourceDataType Management Component', () => {
        let comp: SourceDataTypeComponent;
        let fixture: ComponentFixture<SourceDataTypeComponent>;
        let service: SourceDataTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [SourceDataTypeComponent],
                providers: []
            })
                .overrideTemplate(SourceDataTypeComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SourceDataTypeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SourceDataTypeService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SourceDataType(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.sourceDataTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
